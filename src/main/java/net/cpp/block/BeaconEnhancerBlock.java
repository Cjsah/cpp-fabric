package net.cpp.block;

import net.cpp.block.entity.MobProjectorBlockEntity;
import net.cpp.init.CppStats;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.BlockView;

public class BeaconEnhancerBlock extends AMachineBlock {

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
	public BlockEntity createBlockEntity(BlockView world) {
		return new MobProjectorBlockEntity();
	}
}
