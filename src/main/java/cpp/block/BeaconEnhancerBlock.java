package cpp.block;

import javax.annotation.Nullable;

import cpp.block.entity.BeaconEnhancerBlockEntity;
import cpp.init.CppBlockEntities;
import cpp.init.CppStats;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BeaconEnhancerBlock extends AOutputMachineBlock {
	public BeaconEnhancerBlock() {
		super(Settings.of(Material.WOOD).blockVision((state, world, pos) -> false));
	}
	
	public BeaconEnhancerBlock(Settings settings) {super(settings);
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
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BeaconEnhancerBlockEntity(blockPos, blockState);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(world, type, CppBlockEntities.BEACON_ENHANCER);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends BeaconEnhancerBlockEntity> expectedType) {
		return world.isClient ? null : checkType(givenType, expectedType, BeaconEnhancerBlockEntity::tick);
	}
	@Override
	public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
		return 14;
	}
}
