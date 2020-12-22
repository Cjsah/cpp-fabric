package net.cpp.block.entity;

import net.cpp.block.AMachineBlock;
import net.cpp.init.CppBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EmptyBookshelfBlockEntity extends AMachineBlockEntity {
	public EmptyBookshelfBlockEntity(BlockPos pos, BlockState state) {
		super(CppBlockEntities.DUSTBIN, pos, state);
		setCapacity(3);
	}

	public static void tick(World world, BlockPos pos, BlockState state, EmptyBookshelfBlockEntity blockEntity) {
		if (!world.isClient) {
//			blockEntity.clear();
		}
	}
}
