package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.cpp.api.CodingTool;
import net.cpp.init.CppItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(SilverfishEntity.class)
public class SilverfishEntityMixin extends HostileEntity {
	protected SilverfishEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}
	public boolean isAngryAt(PlayerEntity player) {
		return !player.getInventory().contains(CppItems.SACHET.getDefaultStack()) && super.isAngryAt(player);
	}

	@Inject(at = @At("RETURN"), method = { "initGoals" })
	public void initGoals1(CallbackInfo info) {
		goalSelector.add(1, new CodingTool.SachetFleeGoal(this));
	}
}
