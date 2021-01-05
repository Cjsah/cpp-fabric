package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.cpp.api.CodingTool;
import net.cpp.init.CppItems;
import net.cpp.item.ToolHand;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

@Mixin(ItemFrameEntity.class)
public abstract class MixinItemFrame extends AbstractDecorationEntity {
	protected MixinItemFrame(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract ItemStack getHeldItemStack();

	/**
	 * 当放置了启用的磁铁时，吸引16米以内的物品
	 */
	public void tick() {
		if (!world.isClient) {
			if (getHeldItemStack().isOf(CppItems.MAGNET) && getHeldItemStack().getOrCreateTag().getBoolean("enabled")) {
				CodingTool.attractItems(getPos(), (ServerWorld) world, true, true);

			} else if (getHeldItemStack().isOf(CppItems.TIME_CHECKER)) {
				CodingTool.timeChecker(world);
			} else if (getHeldItemStack().getItem() instanceof ToolHand) {
				((ToolHand) getHeldItemStack().getItem()).tickInItemFrame((ServerWorld) world, getBlockPos(), getHeldItemStack());
			}
		}
		super.tick();
	}
}
