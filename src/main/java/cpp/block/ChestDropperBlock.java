package cpp.block;

import javax.annotation.Nullable;

import cpp.block.entity.ChestDropperBlockEntity;
import cpp.init.CppBlockEntities;
import cpp.init.CppStats;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChestDropperBlock extends AMachineBlock {
	
	public ChestDropperBlock(Settings settings) {super(settings);
	}
	
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ChestDropperBlockEntity(pos, state);
	}

	@Override
	public Identifier getStatIdentifier() {
		return CppStats.INTERACT_WITH_CHEST_DROPPER;
	}
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(world, type, CppBlockEntities.CHEST_DROPPER);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends ChestDropperBlockEntity> expectedType) {
		return world.isClient ? null : checkType(givenType, expectedType, ChestDropperBlockEntity::tick);
	}
}
