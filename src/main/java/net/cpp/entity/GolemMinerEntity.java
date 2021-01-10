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

public class GolemMinerEntity extends AMovingGolemEntity {

	public GolemMinerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void work() {
		BlockState blockState = getBlockState();
		if (!world.isClient) {
			if (!CONTROLS.contains(blockState.getBlock()) && CodingTool.canHarvest(getMainHandStack(), blockState, world, getBlockPos())) {
				List<ItemStack> droppeds = new ArrayList<ItemStack>();
				CodingTool.excavate((ServerWorld) world, this, getBlockPos(), droppeds);
				listMerge(droppeds);
			}
			experience = CodingTool.mend(mainHandStack, experience);
		}
	}

}
