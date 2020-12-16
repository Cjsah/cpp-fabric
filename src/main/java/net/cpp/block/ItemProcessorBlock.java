package net.cpp.block;

import net.cpp.block.entity.ItemProcessorBlockEntity;
import net.cpp.init.CppStats;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ItemProcessorBlock extends AMachineBlock {

	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState
) {
		return new ItemProcessorBlockEntity(blockPos,blockState);
	}

	@Override
	public Identifier getStatIdentifier() {
		return CppStats.INTERACT_WITH_ITEM_PROCESSOR;
	}

}
