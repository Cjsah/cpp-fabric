package net.cpp.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class RiceBlock extends PlantBlock implements Fertilizable {
	public static final IntProperty AGE = Properties.AGE_3;
	public static final List<VoxelShape> AGE_TO_SHAPE = ImmutableList.of(Block.createCuboidShape(0, 0, 0, 16, 7, 16), Block.createCuboidShape(0, 0.0D, 0, 16, 12, 16), Block.createCuboidShape(0, 0.0D, 0, 16, 15, 16), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16, 16.0D));

	public RiceBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(AGE);
		super.appendProperties(builder);
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
		return getDefaultState().with(AGE, 0);
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return AGE_TO_SHAPE.get(state.get(AGE));
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos.down());
		if (blockState.isOf(Blocks.GRASS_BLOCK) || blockState.isOf(Blocks.DIRT) || blockState.isOf(Blocks.COARSE_DIRT) || blockState.isOf(Blocks.PODZOL) || blockState.isOf(Blocks.SAND) || blockState.isOf(Blocks.RED_SAND)) {
			BlockPos blockPos = pos.down();
			Iterator<Direction> var6 = Direction.Type.HORIZONTAL.iterator();
			while (var6.hasNext()) {
				Direction direction = (Direction) var6.next();
				BlockState blockState2 = world.getBlockState(blockPos.offset(direction));
				FluidState fluidState = world.getFluidState(blockPos.offset(direction));
				if (fluidState.isIn(FluidTags.WATER) || blockState2.isOf(Blocks.FROSTED_ICE)) {
					return true;
				}
			}
		}
		return false;
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (!state.canPlaceAt(world, pos)) {
			world.getBlockTickScheduler().schedule(pos, this, 1);
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.canPlaceAt(world, pos)) {
			world.breakBlock(pos, true);
		}
	}
}
