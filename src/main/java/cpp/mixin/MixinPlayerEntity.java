package cpp.mixin;

import cpp.ducktyping.INutrition;
import cpp.ducktyping.IPlayerVaccine;
import cpp.ducktyping.IPlayerWearing;
import cpp.ducktyping.ITemperancable;
import cpp.item.VaccineItem;
import cpp.vaccine.VaccineInstance;
import cpp.vaccine.Vaccines;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements ITemperancable, IPlayerVaccine {

	@Shadow public abstract Iterable<ItemStack> getArmorItems();

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
				this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, value - 1, false, false));
			} else if (value < 0) {
				this.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 2, -value - 1, false, false));
			}
		}
		getActiveStatusEffects().entrySet().removeIf(effect -> {
			boolean remove = VaccineItem.getVaccineEffects((PlayerEntity)(Object)this).contains(effect.getKey());
			if (remove)
				this.onStatusEffectRemoved(effect.getValue());
			return remove;
		});
		this.vaccines.entrySet().removeIf(entry -> !entry.getValue().updateDuration());
		for (ItemStack stack : this.getArmorItems()) {
			if (stack.getItem() instanceof IPlayerWearing item) {
				item.playerWearing(this.world, (PlayerEntity) (Object) this);
			}
		}
	}


	@Inject(at = @At("HEAD"), method = "eatFood")
	public void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
		if (stack.isFood() && stack.getItem() instanceof INutrition) {
			this.weight += ((INutrition)stack.getItem()).getNutrition(stack);
		}
	}

	@Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
	public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
		nbt.putInt("weight", weight);
		nbt.putBoolean("effectEnabled", effectEnabled);
		nbt.put("Vaccines", saveToListNbt());
	}

	@Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
	public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
		weight = nbt.getInt("weight");
		effectEnabled = nbt.getBoolean("effectEnabled");
		for (NbtElement index: nbt.getList("Vaccines", 10)) {
			VaccineInstance vaccine = VaccineInstance.fromNbt((NbtCompound)index);
			this.vaccines.put(vaccine.getVaccine(), vaccine);
		}
	}

	private NbtList saveToListNbt() {
		NbtList list = new NbtList();
		for (Map.Entry<Vaccines, VaccineInstance> index: this.vaccines.entrySet()) {
			list.add(index.getValue().writeNbt(new NbtCompound()));
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
		if (this.vaccines.containsKey(vaccine.getVaccine()) && this.vaccines.get(vaccine.getVaccine()).getDuration() >= vaccine.getDuration())
			return;
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
