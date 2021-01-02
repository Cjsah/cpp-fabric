package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.cpp.api.CodingTool;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.world.World;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends AnimalEntity {

	protected BeeEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("RETURN"), method = { "initGoals" })
	public void initGoals1(CallbackInfo info) {
		goalSelector.add(-1, new CodingTool.SachetFleeGoal(this));
	}
}
