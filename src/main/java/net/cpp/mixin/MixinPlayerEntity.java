package net.cpp.mixin;

import net.cpp.ducktyping.IPlayerVaccine;
import net.cpp.vaccine.VaccineInstance;
import net.cpp.vaccine.Vaccines;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.cpp.ducktyping.INutrition;
import net.cpp.ducktyping.ITemperancable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

@Mixin(PlayerEntity.class)
@SuppressWarnings("unused")
public abstract class MixinPlayerEntity extends LivingEntity implements ITemperancable, IPlayerVaccine {

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	private int weight = 0;
	protected boolean effectEnabled;
	private final Map<Vaccines, VaccineInstance> vaccines = new HashMap<>();


	@Inject(at = @At("RETURN"), method = "tick")
	public void tick(CallbackInfo info) {
		if (!this.world.isClient) {
			int value = weight / 100;
			if (value > 0) {
				this.applyStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, value - 1, false, false));
			} else if (value < 0) {
				this.applyStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 2, -value - 1, false, false));
			}
		}
		this.vaccines.entrySet().removeIf(entry -> !entry.getValue().updateDuration());
	}


	@Inject(at = @At("HEAD"), method = "eatFood")
	public void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
		if (stack.isFood() && stack.getItem() instanceof INutrition) {
			this.weight += ((INutrition) stack.getItem()).getNutrition(stack);
		}
	}

	@Inject(at = @At("HEAD"), method = "writeCustomDataToTag")
	public void writeCustomDataToTag(CompoundTag tag, CallbackInfo info) {
		tag.putInt("weight", weight);
		tag.putBoolean("effectEnabled", effectEnabled);
		tag.put("Vaccines", saveToListTag());
	}

	@Inject(at = @At("HEAD"), method = "readCustomDataFromTag")
	public void readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
		weight = tag.getInt("weight");
		effectEnabled = tag.getBoolean("effectEnabled");
		for (Tag index : tag.getList("Vaccines", 10)) {
			VaccineInstance vaccine = VaccineInstance.fromTag((CompoundTag) index);
			this.vaccines.put(vaccine.getVaccine(), vaccine);
		}
	}

	private ListTag saveToListTag() {
		ListTag list = new ListTag();
		for (Map.Entry<Vaccines, VaccineInstance> index : this.vaccines.entrySet()) {
			list.add(index.getValue().toTag(new CompoundTag()));
		}
		return list;
	}

	public void setEffectEnabled(boolean effectEnabled) {
		this.effectEnabled = effectEnabled;
	}
	public boolean isEffectEnabled() {
		return effectEnabled;
	}

	@Override
	public void addVaccine(VaccineInstance vaccine) {
		if (this.vaccines.containsKey(vaccine.getVaccine()) && this.vaccines.get(vaccine.getVaccine()).getDuration() >= vaccine.getDuration()) return;
		this.vaccines.put(vaccine.getVaccine(), vaccine);
	}

	@Override
	public void removeVaccine(Vaccines vaccine) {
		this.vaccines.remove(vaccine);
	}

	@Override
	public boolean containVaccine(Vaccines vaccine) {
		return this.vaccines.containsKey(vaccine);
	}
}
