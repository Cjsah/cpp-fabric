package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.cpp.api.Utils;
import net.cpp.init.CppItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(EndermiteEntity.class)
@SuppressWarnings("unused")
public class MixinEndermiteEntity extends HostileEntity {

	public MixinEndermiteEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	public boolean isAngryAt(PlayerEntity player) {
		return !player.getInventory().contains(CppItems.SACHET.getDefaultStack()) && super.isAngryAt(player);
	}

	@Inject(at = @At("RETURN"), method = { "initGoals" })
	public void initGoals1(CallbackInfo info) {
		goalSelector.add(1, new Utils.SachetFleeGoal(this));
	}
}
