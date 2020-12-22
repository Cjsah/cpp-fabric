package net.cpp.block;

import net.cpp.block.entity.AExpMachineBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AExpMachineBlock extends AOutputMachineBlock {

	public AExpMachineBlock() {
	}

	public AExpMachineBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient())
			dropExperience((ServerWorld) world, pos, ((AExpMachineBlockEntity) world.getBlockEntity(pos)).getExpStorage());
		super.onBreak(world, pos, state, player);
	}

}
