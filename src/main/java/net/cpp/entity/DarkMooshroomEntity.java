package net.cpp.entity;

import net.cpp.dark.DarkAnimalsFollowTargetGoal;
import net.cpp.dark.DarkAnimalsLookAtEntityGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

import javax.annotation.Nonnull;


public class DarkMooshroomEntity extends ADarkAnimalEntity<MooshroomEntity> {
	private static final TrackedData<String> TYPE = DataTracker.registerData(DarkMooshroomEntity.class, TrackedDataHandlerRegistry.STRING);

	public DarkMooshroomEntity(EntityType<? extends DarkMooshroomEntity> entityType, World world) {
		super(entityType, world, EntityType.MOOSHROOM);
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

	public MooshroomEntity.Type getMooshroomType() {
		return MooshroomEntity.Type.valueOf(this.dataTracker.get(TYPE));
	}

	public void setType(@Nonnull MooshroomEntity.Type type) {
		this.dataTracker.set(TYPE, type.name());
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		this.setType(this.getType(tag.getString("Type")));
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putString("Type", this.getMooshroomType().name());
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(TYPE, MooshroomEntity.Type.RED.name());
	}

	private MooshroomEntity.Type getType(String name) {
		for (MooshroomEntity.Type type : MooshroomEntity.Type.values()) {
			if (type.name().equals(name)) return type;
		}
		return MooshroomEntity.Type.RED;
	}
}
