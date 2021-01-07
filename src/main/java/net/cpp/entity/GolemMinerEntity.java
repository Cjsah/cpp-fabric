package net.cpp.entity;

import java.util.ArrayList;
import java.util.List;

import net.cpp.api.CodingTool;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class GolemMinerEntity extends AMovingGolem {

	public GolemMinerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void work() {
		BlockState blockState = getBlockState();

		if (!world.isClient && !CONTROLS.contains(blockState.getBlock()) && getMainHandStack().getMiningSpeedMultiplier(blockState) > 0) {
			List<ItemStack> droppeds = new ArrayList<ItemStack>();
			CodingTool.excavate((ServerWorld) world, this, getBlockPos(), droppeds);
			for (int i = 0; i < droppeds.size(); i++) {
				droppeds.set(i, inventory.addStack(droppeds.get(i)));
			}
			CodingTool.drop((ServerWorld) world, getPos(), droppeds);
		}
	}

}
