package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.cpp.api.CodingTool;
import net.cpp.init.CppItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameMixin extends AbstractDecorationEntity {
	protected ItemFrameMixin(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract ItemStack getHeldItemStack();

	/**
	 * 当放置了启用的磁铁时，吸引16米以内的物品
	 */
	public void tick() {
		{
			if (!world.isClient&& getHeldItemStack().isOf(CppItems.MAGNET) && getHeldItemStack().getOrCreateTag().getBoolean("enabled")) {
				CodingTool.attractItems(getPos(), (ServerWorld) world, true, true);

			} else if (getHeldItemStack().isOf(CppItems.TIME_CHECKER)) {
				CodingTool.timeChecker(world);
			}
		}
		super.tick();
	}
}
