package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.cpp.api.ITickableInItemFrame;
import net.cpp.ducktype.IRitualStackHolder;
import net.cpp.item.Wand;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

@Mixin(ItemFrameEntity.class)
public abstract class MixinItemFrameEntity extends AbstractDecorationEntity implements IRitualStackHolder {
	private ItemStack ritualStack = ItemStack.EMPTY;

	protected MixinItemFrameEntity(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract ItemStack getHeldItemStack();

	@Shadow
	public abstract void setHeldItemStack(ItemStack stack);

	@Shadow
	public abstract int getRotation();

	public void tick() {
		ItemFrameEntity this0 = (ItemFrameEntity)(Object)this;
		if (!world.isClient) {
			if (getHeldItemStack().getItem() instanceof ITickableInItemFrame) {
				((ITickableInItemFrame) getHeldItemStack().getItem()).tick(this0);
			}
			Wand.tickFrame(this0);
		}
		super.tick();
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		ritualStack.toTag(tag.getCompound("ritualStack"));
		return super.toTag(tag);
	}

	@Override
	public void fromTag(CompoundTag tag) {
		ritualStack = ItemStack.fromTag(tag.getCompound("ritualStack"));
		super.fromTag(tag);
	}
	public void setRitualStack(ItemStack ritualStack) {
		this.ritualStack = ritualStack;
	}
	public ItemStack getRitualStack() {
		return ritualStack;
	}
}
