package net.cpp.block;

import net.cpp.block.entity.ItemProcessorBlockEntity;
import net.cpp.init.CppStats;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.BlockView;

public class ItemProcessorBlock extends AMachineBlock {

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		// TODO 自动生成的方法存根
		return new ItemProcessorBlockEntity();
	}

	@Override
	public Identifier getStatIdentifier() {
		return CppStats.INTERACT_WITH_ITEM_PROCESSOR;
	}

}
