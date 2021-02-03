package net.cpp.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DarkChickenEntity extends HostileEntity {

	public float prevFlapProgress;
	public float flapProgress;
	public float prevMaxWingDeviation;
	public float maxWingDeviation;
	public float flapSpeed = 1.0F;

	public DarkChickenEntity(EntityType<? extends DarkChickenEntity> entityType, World world) {
		super(entityType, world);
	}

	protected void initGoals() {
		this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
		this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0D, false));
		this.targetSelector.add(2, new FollowTargetGoal<PlayerEntity>(this, PlayerEntity.class, true));
	}

	@Override
	protected int getCurrentExperience(PlayerEntity player) {
		return super.getCurrentExperience(player) + 5;
	}

	public boolean tryAttack(Entity target) {
		if (target instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) target;
			living.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 40));
			living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 40));
			return true;
		}
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		if (world.isClient) {
			world.addParticle(ParticleTypes.SMOKE, getX(), getEyeY(), getZ(), (world.random.nextFloat() - .5f) / 10, world.random.nextFloat() / 10, (world.random.nextFloat() - .5f) / 10);
		}
	}

	public void tickMovement() {
		super.tickMovement();
		this.prevFlapProgress = this.flapProgress;
		this.prevMaxWingDeviation = this.maxWingDeviation;
		this.maxWingDeviation = (float) ((double) this.maxWingDeviation + (double) (this.onGround ? -1 : 4) * 0.3D);
		this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0F, 1.0F);
		if (!this.onGround && this.flapSpeed < 1.0F) {
			this.flapSpeed = 1.0F;
		}

		this.flapSpeed = (float) ((double) this.flapSpeed * 0.9D);
		Vec3d vec3d = this.getVelocity();
		if (!this.onGround && vec3d.y < 0.0D) {
			this.setVelocity(vec3d.multiply(1.0D, 0.6D, 1.0D));
		}

		this.flapProgress += this.flapSpeed * 2.0F;

	}
}
