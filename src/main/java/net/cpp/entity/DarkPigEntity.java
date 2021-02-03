package net.cpp.entity;

import java.util.EnumSet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class DarkPigEntity extends HostileEntity {
	private static final TrackedData<Integer> FUSE_SPEED = DataTracker.registerData(DarkPigEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private int lastFuseTime;
	private int currentFuseTime;
	private int fuseTime = 30;
	private int explosionRadius = 3;

	public DarkPigEntity(EntityType<? extends DarkPigEntity> entityType, World world) {
		super(entityType, world);
	}

	protected void initGoals() {
		this.goalSelector.add(1, new SwimGoal(this));
		goalSelector.add(2, new IgniteGoal(this));
		this.goalSelector.add(4, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8D));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(6, new LookAroundGoal(this));
		this.targetSelector.add(1, new FollowTargetGoal<PlayerEntity>(this, PlayerEntity.class, true));
		this.targetSelector.add(2, new RevengeGoal(this, new Class[0]));
	}

	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putShort("Fuse", (short) this.fuseTime);
		tag.putByte("ExplosionRadius", (byte) this.explosionRadius);
	}

	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		if (tag.contains("Fuse", 99)) {
			this.fuseTime = tag.getShort("Fuse");
		}
		if (tag.contains("ExplosionRadius", 99)) {
			this.explosionRadius = tag.getByte("ExplosionRadius");
		}
	}

	@Override
	protected int getCurrentExperience(PlayerEntity player) {
		return super.getCurrentExperience(player) + 5;
	}

	public boolean tryAttack(Entity target) {
		return true;
	}

	@Override
	public void tick() {
		if (this.isAlive()) {
			this.lastFuseTime = this.currentFuseTime;

			int i = this.getFuseSpeed();
			if (i > 0 && this.currentFuseTime == 0) {
				this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
			}

			this.currentFuseTime += i;
			if (this.currentFuseTime < 0) {
				this.currentFuseTime = 0;
			}

			if (this.currentFuseTime >= this.fuseTime) {
				this.currentFuseTime = this.fuseTime;
				this.explode();
			}
		}

		super.tick();
		if (world.isClient) {
			world.addParticle(ParticleTypes.SMOKE, getX(), getEyeY(), getZ(), (world.random.nextFloat() - .5f) / 10, world.random.nextFloat() / 10, (world.random.nextFloat() - .5f) / 10);
		}

	}

	public int getFuseSpeed() {
		return this.dataTracker.get(FUSE_SPEED);
	}

	public void setFuseSpeed(int fuseSpeed) {
		this.dataTracker.set(FUSE_SPEED, fuseSpeed);
	}

	private void explode() {
		if (!this.world.isClient) {
			Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
			this.dead = true;
			this.world.createExplosion(this, this.getX(), this.getY(), this.getZ(), this.explosionRadius, destructionType);
			this.discard();
		}

	}

	@Environment(EnvType.CLIENT)
	public float getClientFuseTime(float timeDelta) {
		return MathHelper.lerp(timeDelta, (float) this.lastFuseTime, (float) this.currentFuseTime) / (float) (this.fuseTime - 2);
	}

	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(FUSE_SPEED, -1);
	}

	public static class IgniteGoal extends Goal {
		private final DarkPigEntity darkPig;
		private LivingEntity target;

		public IgniteGoal(DarkPigEntity darkPig) {
			this.darkPig = darkPig;
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}

		public boolean canStart() {
			LivingEntity livingEntity = this.darkPig.getTarget();
			return this.darkPig.getFuseSpeed() > 0 || livingEntity != null && this.darkPig.squaredDistanceTo(livingEntity) < 9.0D;
		}

		public void start() {
			this.darkPig.getNavigation().stop();
			this.target = this.darkPig.getTarget();
		}

		public void stop() {
			this.target = null;
		}

		public void tick() {
			if (this.target == null) {
				this.darkPig.setFuseSpeed(-1);
			} else if (this.darkPig.squaredDistanceTo(this.target) > 49.0D) {
				this.darkPig.setFuseSpeed(-1);
			} else if (!this.darkPig.getVisibilityCache().canSee(this.target)) {
				this.darkPig.setFuseSpeed(-1);
			} else {
				this.darkPig.setFuseSpeed(1);
			}
		}
	}
}
