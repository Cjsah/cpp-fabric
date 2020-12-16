package net.cpp.block;

import net.cpp.block.entity.BeaconEnhancerBlockEntity;
import net.cpp.init.CppStats;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class BeaconEnhancerBlock extends AMachineBlock {
	public BeaconEnhancerBlock() {
		
	}
	/*
	 * 以下是AMachineBlock的方法
	 */
	@Override
	public Identifier getStatIdentifier() {
		return CppStats.INTERACT_WITH_BEACON_ENHANCER;
	}

	/*
	 * 以下是BlockEntityProvider的方法
	 */
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState
) {
		return new BeaconEnhancerBlockEntity(blockPos,blockState);
	}
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
}
