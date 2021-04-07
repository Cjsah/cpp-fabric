package cpp.block;

import javax.annotation.Nullable;

import cpp.block.entity.EmptyBookshelfBlockEntity;
import cpp.init.CppBlockEntities;
import cpp.init.CppStats;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EmptyBookshelfBlock extends AMachineBlock {
	public static final IntProperty BOOK_STATE = IntProperty.of("book_state", 0b000, 0b111);

	public EmptyBookshelfBlock() {
		setDefaultState(stateManager.getDefaultState().with(BOOK_STATE, 0));
	}
	
	public EmptyBookshelfBlock(Settings settings) {super(settings);
	}
	
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new EmptyBookshelfBlockEntity(pos, state);
	}

	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(BOOK_STATE);
	}

	@Override
	public Identifier getStatIdentifier() {
		return CppStats.INTERACT_WITH_EMPTY_BOOKSHELF;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(world, type, CppBlockEntities.EMPTY_BOOKSHELF);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends EmptyBookshelfBlockEntity> expectedType) {
		return world.isClient ? null : checkType(givenType, expectedType, EmptyBookshelfBlockEntity::tick);
	}
}
