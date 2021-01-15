package net.cpp.block;

import javax.annotation.Nullable;
import static net.cpp.init.CppItems.*;
import net.cpp.block.entity.AllInOneMachineBlockEntity;
import net.cpp.block.entity.AllInOneMachineBlockEntity.Degree;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppStats;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
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

	/**
	 * 掉落已安装的插件
	 */
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity blockEntity0 = world.getBlockEntity(pos);
		if (blockEntity0 instanceof AllInOneMachineBlockEntity) {
			AllInOneMachineBlockEntity blockEntity = (AllInOneMachineBlockEntity) blockEntity0;
			if (blockEntity.isAvailabePressure(Degree.LOW)) {
				dropStack(world, pos, LOW_PRESSURE_PLUGIN.getDefaultStack());
			}
			if (blockEntity.isAvailabePressure(Degree.HIGH)) {
				dropStack(world, pos, HIGH_PRESSURE_PLUGIN.getDefaultStack());
			}
			if (blockEntity.isAvailabeTemperature(Degree.LOW)) {
				dropStack(world, pos, LOW_TEMPERATURE_PLUGIN.getDefaultStack());
			}
			if (blockEntity.isAvailabeTemperature(Degree.HIGH)) {
				dropStack(world, pos, HIGH_TEMPERATURE_PLUGIN.getDefaultStack());
			}
		}
		super.onBreak(world, pos, state, player);
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
