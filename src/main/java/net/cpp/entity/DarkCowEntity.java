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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class DarkCowEntity extends HostileEntity {

	public DarkCowEntity(EntityType<? extends DarkCowEntity> entityType, World world) {
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
			living.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 140));
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
}
