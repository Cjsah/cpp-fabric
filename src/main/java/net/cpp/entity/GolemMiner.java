package net.cpp.entity;

import java.util.LinkedList;
import java.util.List;

import net.cpp.api.CodingTool;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class GolemMiner extends AMovingGolem {

	public GolemMiner(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void work() {
		if (!world.isClient && !CONTROLS.contains(getBlockState().getBlock())) {
			List<ItemStack> droppeds = new LinkedList<ItemStack>();
			CodingTool.excavate((ServerWorld) world, this, getBlockPos(), droppeds);
			for (ItemStack stack : droppeds) {
				inventory.addStack(stack);
			}
			CodingTool.drop((ServerWorld) world, getPos(), droppeds);
		}
	}

}
