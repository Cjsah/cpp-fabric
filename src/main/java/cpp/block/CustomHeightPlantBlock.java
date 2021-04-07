package cpp.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class CustomHeightPlantBlock extends PlantBlock {
	private static IntProperty mjsbTempProperty;
	public final IntProperty part;
	public final int height;

	protected CustomHeightPlantBlock(Settings settings) {
		super(settings);
		part = mjsbTempProperty;
		height = part.getValues().size();
	}

	public static CustomHeightPlantBlock of(int height, Settings settings) {
		mjsbTempProperty = IntProperty.of("part", 0, height - 1);
		return new CustomHeightPlantBlock(settings);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		int o = state.get(part);
		for (int i = 0; i < height; i++) {
			BlockPos pos2 = pos.add(0, i - o, 0);
			BlockState state2 = world.getBlockState(pos2);
			boolean b = state2.isOf(this) && state2.get(part) == i;
			if (posFrom.equals(pos2) && b)
				return state;
			if (!b)
				return Blocks.AIR.getDefaultState();
		}
		return state;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState();
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		int o = state.get(part);
		for (int i = 0; i < height; i++) {
			BlockPos pos2 = pos.add(0, i - o, 0);
			world.setBlockState(pos2, state.with(part, i));
		}
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		int o = state.get(part);
		for (int i = 0; i < height; i++) {
			BlockPos pos2 = pos.add(0, i - o, 0);
			if (!world.getBlockState(pos2).isAir()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		// TODO 自动生成的方法存根
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
		// TODO 自动生成的方法存根
		super.afterBreak(world, player, pos, state, blockEntity, stack);
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(part == null ? mjsbTempProperty : part);
	}
}
