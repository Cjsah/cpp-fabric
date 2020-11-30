package net.cpp.block;

import net.cpp.Registeror;
import net.cpp.block.entity.CraftingMachineBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.BlockView;

public class AllInOneMachineBlock extends AMachineBlock {
	/*
	 * 以下是AMachineBlock的方法
	 */
	@Override
	public Identifier getStatIdentifier() {
		return Registeror.INTERACT_WITH_ALL_IN_ONE_MACHINE;
	}
	/*
	 * 以下是BlockEntityProvider的方法
	 */
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new CraftingMachineBlockEntity(); //TODO
	}
}
