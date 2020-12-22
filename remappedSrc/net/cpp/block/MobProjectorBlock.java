package net.cpp.block;

import javax.annotation.Nullable;

import net.cpp.block.entity.MobProjectorBlockEntity;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppStats;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MobProjectorBlock extends AExpMachineBlock {

	public MobProjectorBlock() {
	}

	/*
	 * 以下是AMachineBlock的方法
	 */
	@Override
	public Identifier getStatIdentifier() {
		return CppStats.INTERACT_WITH_MOB_PROJECTOR;
	}

	/*
	 * 以下是BlockEntityProvider的方法
	 */
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new MobProjectorBlockEntity(blockPos, blockState);
	}
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(world, type, CppBlockEntities.MOB_PROJECTOR);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends MobProjectorBlockEntity> expectedType) {
		return world.isClient ? null : checkType(givenType, expectedType, MobProjectorBlockEntity::tick);
	}
}
