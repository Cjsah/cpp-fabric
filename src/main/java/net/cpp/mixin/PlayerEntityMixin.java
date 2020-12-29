package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.cpp.api.CodingTool;
import net.cpp.init.CppItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract PlayerInventory getInventory();

	/**
	 * 当玩家携带启用的磁铁时，吸引16米内的物品
	 * 
	 * @param callbackInfo
	 */
	@Inject(at = @At("HEAD"), method = "tick()V")
	public void tick(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			boolean enabled = false;
			for (int i = 0; i < getInventory().size(); i++) {
				ItemStack itemStack = getInventory().getStack(i);
				if (itemStack.isOf(CppItems.MAGNET) && itemStack.getOrCreateTag().getBoolean("enabled")) {
					enabled = true;
					break;
				}
			}
			if (enabled) {
				CodingTool.attractItems(getPos().add(0, 1, 0), (ServerWorld) world, true, false);
			}
		}
	}

}
