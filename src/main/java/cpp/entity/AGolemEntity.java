package cpp.entity;

import java.util.Collections;

import cpp.api.Utils;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class AGolemEntity extends LivingEntity {
	protected ItemStack mainHandStack = ItemStack.EMPTY;
	protected boolean killed;

	public AGolemEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
		setNoGravity(true);
		setInvulnerable(true);
		noClip = true;
	}

	@Override
	public Iterable<ItemStack> getArmorItems() {
		return Collections.emptyList();
	}

	@Override
	public ItemStack getEquippedStack(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? mainHandStack : ItemStack.EMPTY;
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {
		if (slot == EquipmentSlot.MAINHAND)
			mainHandStack = stack;
	}

	@Override
	public Arm getMainArm() {
		return Arm.RIGHT;
	}

	@Override
	public void readNbt(NbtCompound tag) {
		mainHandStack = ItemStack.fromNbt(tag.getCompound("mainHandStack"));
		super.readNbt(tag);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		tag.put("mainHandStack", mainHandStack.writeNbt(new NbtCompound()));
		return super.writeNbt(tag);
	}

	public abstract void work();

	public void setMainHandStack(ItemStack mainHandStack) {
		this.mainHandStack = mainHandStack;
	}

	protected void reactBlock() {
		if (world.getBlockState(getBlockPos().down()).isAir()) {
			killed = true;
		}
	}

	@Override
	public void tick() {
		reactBlock();
		work();
		super.tick();
		if (killed) {
			kill();
		}
	}

	@Override
	public double getHeightOffset() {
		return 0;
	}

	@Override
	public PistonBehavior getPistonBehavior() {
		return PistonBehavior.IGNORE;
	}

	@Override
	public boolean collides() {
		return false;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		return false;
	}

	@Override
	public void kill() {
		Utils.drop(world, getPos(), mainHandStack);
		discard();
	}

	@Override
	protected boolean isImmobile() {
		return false;
	}

	@Override
	public void tickMovement() {
		if (this.isLogicalSideForUpdatingMovement()) {
			this.bodyTrackingIncrements = 0;
			this.updateTrackedPosition(this.getX(), this.getY(), this.getZ());
		}
		if (this.bodyTrackingIncrements > 0) {
			double d = this.getX() + (this.serverX - this.getX()) / (double) this.bodyTrackingIncrements;
			double e = this.getY() + (this.serverY - this.getY()) / (double) this.bodyTrackingIncrements;
			double f = this.getZ() + (this.serverZ - this.getZ()) / (double) this.bodyTrackingIncrements;
			double g = MathHelper.wrapDegrees(this.serverYaw - (double) this.yaw);
			this.yaw = (float) ((double) this.yaw + g / (double) this.bodyTrackingIncrements);
			this.pitch = (float) ((double) this.pitch + (this.serverPitch - (double) this.pitch) / (double) this.bodyTrackingIncrements);
			--this.bodyTrackingIncrements;
			this.updatePosition(d, e, f);
			this.setRotation(this.yaw, this.pitch);
		}
	}
}
