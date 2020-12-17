package net.cpp.block;

import javax.annotation.Nullable;

import net.cpp.block.entity.AllInOneMachineBlockEntity;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppStats;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AllInOneMachineBlock extends AExpMachineBlock {
	public static final BooleanProperty WORKING = BooleanProperty.of("working");

	public AllInOneMachineBlock() {
		setDefaultState(stateManager.getDefaultState().with(WORKING, false));
	}

	/*
	 * 以下是AMachineBlock的方法
	 */
	@Override
	public Identifier getStatIdentifier() {
		return CppStats.INTERACT_WITH_ALL_IN_ONE_MACHINE;
	}

	/*
	 * 以下是Block的方法
	 */
	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(WORKING);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AllInOneMachineBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(world, type, CppBlockEntities.ALL_IN_ONE_MACHINE);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends AllInOneMachineBlockEntity> expectedType) {
		return world.isClient ? null : checkType(givenType, expectedType, AllInOneMachineBlockEntity::tick);
	}
}
