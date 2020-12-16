package net.cpp.block;

import net.cpp.block.entity.AllInOneMachineBlockEntity;
import net.cpp.init.CppStats;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AllInOneMachineBlock extends AMachineBlock {
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
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient())
			dropExperience((ServerWorld) world, pos,
					((AllInOneMachineBlockEntity) world.getBlockEntity(pos)).getExpStorage());
		super.onBreak(world, pos, state, player);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		// TODO 自动生成的方法存根
		return new AllInOneMachineBlockEntity(pos,state);
	}

}
