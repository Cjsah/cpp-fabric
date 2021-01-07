package net.cpp.entity;

import java.util.Collections;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AGolemEntity extends LivingEntity {
	protected ItemStack mainHandStack = ItemStack.EMPTY;

	public AGolemEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
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
	public void fromTag(CompoundTag tag) {
		mainHandStack = ItemStack.fromTag(tag.getCompound("mainHandStack"));
		super.fromTag(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("mainHandStack", mainHandStack.toTag(new CompoundTag()));
		return super.toTag(tag);
	}
	public abstract void work();

}
