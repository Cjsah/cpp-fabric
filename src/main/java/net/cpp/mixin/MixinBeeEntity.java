package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.cpp.api.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.world.World;

@Mixin(BeeEntity.class)
@SuppressWarnings("unused")
public abstract class MixinBeeEntity extends AnimalEntity {

	protected MixinBeeEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("RETURN"), method = { "initGoals" })
	public void initGoals1(CallbackInfo info) {
		goalSelector.add(-1, new Utils.SachetFleeGoal(this));
	}
}
