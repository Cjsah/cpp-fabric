package net.cpp.entity;

import net.cpp.dark.DarkAnimalsFollowTargetGoal;
import net.cpp.dark.DarkAnimalsLookAtEntityGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class DarkCowEntity extends ADarkAnimalEntity<CowEntity> {

	public DarkCowEntity(EntityType<? extends DarkCowEntity> entityType, World world) {
		super(entityType, world, EntityType.COW);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(8, new DarkAnimalsLookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
		this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0D, false));
		this.targetSelector.add(2, new DarkAnimalsFollowTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	public boolean tryAttack(Entity target) {
		if (target instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) target;
			living.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 40));
			living.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 140));
			return true;
		}
		return false;
	}
}
