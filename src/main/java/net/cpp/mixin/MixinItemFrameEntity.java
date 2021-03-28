package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.cpp.ducktyping.ITickableInItemFrame;
import net.cpp.item.Wand;
import net.cpp.item.Wand.IRitualFrame;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

@Mixin(ItemFrameEntity.class)
@SuppressWarnings("unused")
public abstract class MixinItemFrameEntity extends AbstractDecorationEntity implements IRitualFrame {
	private int ritualType, ritualTime;

	protected MixinItemFrameEntity(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract ItemStack getHeldItemStack();

	@Shadow
	public abstract void setHeldItemStack(ItemStack stack);

	@Shadow
	public abstract int getRotation();

	@SuppressWarnings("ConstantConditions")
	public void tick() {
		ItemFrameEntity this0 = (ItemFrameEntity) (Object) this;
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
		tag.putInt("ritualTime", ritualTime);
		tag.putInt("ritualType", ritualType);
		return super.toTag(tag);
	}

	@Override
	public void fromTag(CompoundTag tag) {
		ritualTime = tag.getInt("ritualTime");
		ritualType = tag.getInt("ritualType");
		super.fromTag(tag);
	}

	public void setRitualType(int type) {
		this.ritualType = type;
	}

	public int getRitualType() {
		return ritualType;
	}

	public void setRitualTime(int time) {
		this.ritualTime = time;
	}

	public int getRitualTime() {
		return ritualTime;
	}
}
