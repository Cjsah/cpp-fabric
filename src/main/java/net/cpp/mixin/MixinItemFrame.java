package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.cpp.api.IRitualStackHolder;
import net.cpp.item.ITickableInItemFrame;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

@Mixin(ItemFrameEntity.class)
public abstract class MixinItemFrame extends AbstractDecorationEntity implements IRitualStackHolder {
	private ItemStack ritualStack = ItemStack.EMPTY;

	protected MixinItemFrame(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract ItemStack getHeldItemStack();

	@Shadow
	public abstract void setHeldItemStack(ItemStack stack);

	@Shadow
	public abstract int getRotation();

	public void tick() {
		if (!world.isClient) {
			if (getHeldItemStack().getItem() instanceof ITickableInItemFrame) {
				((ITickableInItemFrame) getHeldItemStack().getItem()).tick((ItemFrameEntity) (Object) this);
			}
			if (!ritualStack.isEmpty()) {
				int delay = ritualStack.getOrCreateTag().getInt("delay");
				if (delay-- <= 0) {
					ritualStack.removeSubTag("delay");
					setHeldItemStack(ritualStack);
					ritualStack = ItemStack.EMPTY;
					setInvulnerable(false);
				} else {
					ritualStack.getOrCreateTag().putInt("delay", delay);
					for (ServerPlayerEntity player : ((ServerWorld) world).getPlayers(player -> player.getPos().isInRange(getPos(), 32))) {
						player.networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.ENCHANT, false, getX(), getY() + 1, getZ(), 0.2f, 0, 0.2f, 1, 1));
					}
				}
			}
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
