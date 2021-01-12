package net.cpp.block;

import java.util.Random;

import net.cpp.Craftingpp;
import net.cpp.api.CodingTool;
import net.cpp.init.CppBlocks;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class FruitLeavesBlock extends LeavesBlock {
	public static final Tag<Item> FRUITS = TagRegistry.item(new Identifier(Craftingpp.MOD_ID3, "fruits"));
	public static final Tag<Item> DROPPABLE_FRUITS = TagRegistry.item(new Identifier(Craftingpp.MOD_ID3, "droppable_fruits"));
	public static int speed = 3600;

	public FruitLeavesBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return !state.get(PERSISTENT);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (random.nextDouble() < 1. / 20 / speed) {
			Pair<BlockPos, Integer> pair;
			BlockPos pos2 = pos, pos3 = pos2;
			int distance = state.get(DISTANCE);
			int limit = 10;
			do {
				pos3 = pos2;
				pair = maxDistance(world, pos2, distance);
				pos2 = pair.getLeft();
				distance = pair.getRight();
			} while (pos3 != pos2 && limit-- > 0);
			world.removeBlock(pos2, false);
			CodingTool.drop(world, new Vec3d(pos2.getX() + .5, pos2.getY() + .5, pos2.getZ() + .5), randomFruit(random));
		} else
			super.randomTick(state, world, pos, random);
	}

	private static Pair<BlockPos, Integer> maxDistance(ServerWorld world, BlockPos pos, int distance) {
		while (world.getBlockState(pos.down()).isOf(CppBlocks.FRUIT_LEAVES)) {
			pos = pos.down();
		}
		Direction[] directions = Direction.values();
		BlockPos pos2 = pos;
		for (Direction direction : directions) {
			BlockPos pos3 = pos.add(direction.getVector());
			BlockState state2 = world.getBlockState(pos3);
			if (state2.isOf(CppBlocks.FRUIT_LEAVES)) {
				int d2 = state2.get(DISTANCE);
				if (distance < d2) {
					distance = d2;
					pos2 = pos3;
				}
			}
		}
		return new Pair<>(pos2, distance);
	}

	public static ItemStack randomFruit(Random random) {
		if (DROPPABLE_FRUITS.values().isEmpty())
			return ItemStack.EMPTY;
		return DROPPABLE_FRUITS.getRandom(random).getDefaultStack();
	}

	static {
	}
}
