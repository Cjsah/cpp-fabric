package cpp.mixin;

import cpp.ducktyping.IPlayerVaccine;
import cpp.vaccine.VaccineInstance;
import cpp.vaccine.Vaccines;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cpp.ducktyping.INutrition;
import cpp.ducktyping.ITemperancable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

	@Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
	public void writeCustomDataToNbt(NbtCompound tag, CallbackInfo info) {
		tag.putInt("weight", weight);
		tag.putBoolean("effectEnabled", effectEnabled);
		tag.put("Vaccines", saveToListTag());
	}

	@Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
	public void readCustomDataFromNbt(NbtCompound tag, CallbackInfo info) {
		weight = tag.getInt("weight");
		effectEnabled = tag.getBoolean("effectEnabled");
		for (NbtElement index : tag.getList("Vaccines", 10)) {
			VaccineInstance vaccine = VaccineInstance.fromTag((NbtCompound) index);
			this.vaccines.put(vaccine.getVaccine(), vaccine);
		}
	}

	private NbtList saveToListTag() {
		NbtList list = new NbtList();
		for (Map.Entry<Vaccines, VaccineInstance> index : this.vaccines.entrySet()) {
			list.add(index.getValue().toTag(new NbtCompound()));
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
