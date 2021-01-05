package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.cpp.item.ITickableInItemFrame;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(ItemFrameEntity.class)
public abstract class MixinItemFrame extends AbstractDecorationEntity {
	protected MixinItemFrame(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract ItemStack getHeldItemStack();

	@Shadow
	public abstract int getRotation();

	/**
	 * 当放置了启用的磁铁时，吸引16米以内的物品
	 */
	public void tick() {
		if (!world.isClient) {
			if (getHeldItemStack().getItem() instanceof ITickableInItemFrame) {
				((ITickableInItemFrame) getHeldItemStack().getItem()).tick((ItemFrameEntity)(Object)this);
			}
		}
		super.tick();
	}
}
