package net.cpp.block.entity;

import net.cpp.init.CppBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class DustbinBlockEntity extends BarrelBlockEntity {

	public DustbinBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}
	@Override
	protected Text getContainerName() {
		return CppBlocks.DUSTBIN.getName();
	}
}
