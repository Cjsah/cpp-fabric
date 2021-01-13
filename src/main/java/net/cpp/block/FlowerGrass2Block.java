package net.cpp.block;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class FlowerGrass2Block extends PlantBlock implements Fertilizable {
	public FlowerGrass2Block(Settings settings) {
		super(settings);
	}

	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.XZ;
	}

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		dropStack(world, pos, asItem().getDefaultStack());
	}
}
