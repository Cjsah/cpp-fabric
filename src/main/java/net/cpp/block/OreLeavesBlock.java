package net.cpp.block;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.google.common.collect.Maps;

import static net.minecraft.block.Blocks.*;

import net.cpp.init.CppBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class OreLeavesBlock extends LeavesBlock {
	private static final Map<Block, Integer> ORES = Maps.newLinkedHashMap();
	private static int weights;
	public static int speed = 3600;

	public OreLeavesBlock(Settings settings) {
		super(settings);
	}

	protected static BlockState updateDistanceFromLogs(BlockState state, WorldAccess world, BlockPos pos) {
		int i = 7;
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		Direction[] var5 = Direction.values();
		int var6 = var5.length;

		for (int var7 = 0; var7 < var6; ++var7) {
			Direction direction = var5[var7];
			mutable.set(pos, direction);
			i = Math.min(i, getDistanceFromLog(world.getBlockState(mutable)) + 1);
			if (i == 1) {
				break;
			}
		}

		return state.with(DISTANCE, i);
	}

	public static int getDistanceFromLog(BlockState state) {
		if (state.isIn(BlockTags.LOGS) || ORES.containsKey(state.getBlock())) {
			return 0;
		} else {
			return state.getBlock() instanceof LeavesBlock ? (Integer) state.get(DISTANCE) : 7;
		}
	}

	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.getBlock() == CppBlocks.ORE_LEAVES)
			world.setBlockState(pos, updateDistanceFromLogs(state, world, pos), 3);
	}

	private static void addOre(Block ore, int weight) {
		ORES.put(ore, weight);
		weights += weight;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.random.nextDouble() < 4096. / 20 / speed)
			world.setBlockState(pos, randomOre().getDefaultState());
		else
			super.randomTick(state, world, pos, random);
	}

	public boolean hasRandomTicks(BlockState state) {
		return true;
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		int i = getDistanceFromLog(newState) + 1;
		if (i != 1 || state.get(DISTANCE) != i) {
			world.getBlockTickScheduler().schedule(pos, this, 1);
		}

		return state;
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return updateDistanceFromLogs(getDefaultState().with(PERSISTENT, true), ctx.getWorld(), ctx.getBlockPos());
	}

	public static Block randomOre() {
		int r = (int) (Math.random() * weights);
		for (Entry<Block, Integer> entry : ORES.entrySet()) {
			if (r < entry.getValue()) {
				return entry.getKey();
			} else {
				r -= entry.getValue();
			}
		}
		return AIR;
	}

	static {
		addOre(ANCIENT_DEBRIS, 5);
		addOre(EMERALD_ORE, 5);
		addOre(DIAMOND_ORE, 10);
		addOre(LAPIS_ORE, 10);
		addOre(REDSTONE_ORE, 50);
		addOre(GOLD_ORE, 20);
		addOre(NETHER_GOLD_ORE, 20);
		addOre(GILDED_BLACKSTONE, 10);
		addOre(NETHER_QUARTZ_ORE, 180);
		addOre(IRON_ORE, 180);
		addOre(COPPER_ORE, 220);
		addOre(COAL_ORE, 290);
	}
}
