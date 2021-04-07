package cpp.block;

import javax.annotation.Nullable;

import cpp.block.entity.ItemProcessorBlockEntity;
import cpp.init.CppBlockEntities;
import cpp.init.CppStats;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemProcessorBlock extends AOutputMachineBlock {
	
	public ItemProcessorBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public Identifier getStatIdentifier() {
		return CppStats.INTERACT_WITH_ITEM_PROCESSOR;
	}
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new ItemProcessorBlockEntity(blockPos, blockState);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(world, type, CppBlockEntities.ITEM_PROCESSER);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends ItemProcessorBlockEntity> expectedType) {
		return world.isClient ? null : checkType(givenType, expectedType, ItemProcessorBlockEntity::tick);
	}
}
