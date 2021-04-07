package cpp.block;

import javax.annotation.Nullable;

import cpp.block.entity.TradeMachineBlockEntity;
import cpp.init.CppBlockEntities;
import cpp.init.CppStats;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TradeMachineBlock extends AExpMachineBlock {
	public TradeMachineBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TradeMachineBlockEntity(pos, state);
	}

	@Override
	public Identifier getStatIdentifier() {
		return CppStats.INTERACT_WITH_TRADE_MACHINE;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(world, type, CppBlockEntities.TRADE_MACHINE);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends TradeMachineBlockEntity> expectedType) {
		return world.isClient ? null : checkType(givenType, expectedType, TradeMachineBlockEntity::tick);
	}
}
