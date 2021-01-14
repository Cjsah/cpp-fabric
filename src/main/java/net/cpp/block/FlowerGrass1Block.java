package net.cpp.block;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import net.cpp.init.CppItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class FlowerGrass1Block extends FlowerBlock implements Fertilizable {
	public static final IntProperty AGE = Properties.AGE_3;
	public static final List<VoxelShape> AGE_TO_SHAPE = ImmutableList.of(Block.createCuboidShape(3, 0, 3, 13, 10, 13), Block.createCuboidShape(2, 0.0D, 2, 14, 12, 14), Block.createCuboidShape(1, 0.0D, 1, 15, 14, 15), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16, 16.0D));
	private Item seed;

	public FlowerGrass1Block(StatusEffect suspiciousStewEffect, int effectDuration, Settings settings) {
		super(suspiciousStewEffect, effectDuration, settings);
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(AGE);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return state.get(AGE) < 3;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		grow(world, random, pos, state);
	}

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return hasRandomTicks(state);
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return hasRandomTicks(state);
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		world.setBlockState(pos, state.with(AGE, state.get(AGE) + 1));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(AGE, ctx.getStack().isOf(asItem()) ? 3 : 0);
	}

	public void setSeed(Item seed) {
		this.seed = seed;
	}

	public Item getSeed() {
		if (seed == null)
			CppItems.register();
		return seed;
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return AGE_TO_SHAPE.get(state.get(AGE));
	}
}
