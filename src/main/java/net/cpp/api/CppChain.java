package net.cpp.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import io.netty.util.internal.IntegerHolder;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext.Builder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CppChain {
	public static final Map<Item, Set<Block>> TOOL_TO_BLOCK = new HashMap<>();
	static {
		// TODO
		
	}

	public static Set<Block> searchBlockByKeyWord(String keyWord) {
		Set<Block> set = new HashSet<Block>();

		return set;
	}

	/**
	 * 连锁
	 *
	 * @param player 挖掘者
	 * @param pos    挖掘原点
	 * @param block  挖掘的方块
	 */
	public static void chain(World world, ServerPlayerEntity player, BlockPos pos, Block block) {
		ListTag enchants = player.getMainHandStack().getEnchantments().copy();
		boolean silk_torch = false;
		boolean fortune = false;
		short fortune_level = 0;
		for (Tag enchant : enchants) {
			CompoundTag tag = (CompoundTag) enchant;
			if (Objects.equals(tag.getString("id"), "minecraft:silk_touch")) {
				silk_torch = true;
			}
			if (Objects.equals(tag.getString("id"), "minecraft:fortune")) {
				fortune = true;
				fortune_level = tag.getShort("lvl");
			}
		}
		int count = dfsChain(world, player, pos, block, 0);
		if (count > 0) {
			if (fortune && fortune_level > 0) {
				int value = 0;
				for (int i = 0; i < count; i++) {
					value += new Random().nextInt(fortune_level + 1) + 1;
				}
				spawn(world, pos, block, value, false);
			} else {
				spawn(world, pos, block, count, silk_torch);
			}
		}
	}

    /**
     * 深搜
     * (栈溢出利器)
     *
     * @param player    挖掘者
     * @param pos       挖掘点
     * @param block     挖掘的方块
     * @param count     连锁次数
     * @return          连锁次数
     */
    private static int dfsChain(World world, ServerPlayerEntity player, BlockPos pos, Block block, int count) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (world.getBlockState(pos.add(i,j,k)).getBlock() == block && player.getPos().distanceTo(Vec3d.ofCenter(pos.add(i,j,k))) < 16 && count < 32767) {
                        world.setBlockState(pos.add(i,j,k), Blocks.AIR.getDefaultState());
                        count = dfsChain(world, player, pos.add(i,j,k), block, ++count);
                    }
                }
            }
        }
        return count;
    }

	/**
	 * 广搜
	 *
	 * @param player 挖掘者
	 * @param pos    挖掘原点
	 * @param block  挖掘的方块
	 * @param count  连锁次数
	 * @return 连锁次数
	 */
	private static int bfsChain(ServerWorld world, ServerPlayerEntity player, BlockPos pos, BlockState blockState, int count, List<ItemStack> itemStacks, IntegerHolder integerHolder) {
		Queue<BlockPos> queue = new LinkedList<>();
		queue.offer(pos);
		while (!queue.isEmpty() && count > 0) {
			BlockPos pos2 = queue.poll();
			itemStacks.addAll(Block.getDroppedStacks(blockState, world, pos, null, player, player.getMainHandStack()));
			
			world.breakBlock(pos, true, player);
			for1: for (int i = -1; i <= 1; i++)
				for (int j = -1; j <= 1; j++)
					for (int k = -1; k <= 1; k++) {
						if (i != 0 || j != 0 || k != 0) {
							BlockPos pos3 = pos2.add(i, j, k);
							if (world.getBlockState(pos3).isOf(blockState.getBlock())) {
								queue.offer(pos3);
							}
						}
						if (count-- <= 0) {
							break for1;
						}
					}
		}
		return count;
	}

	private static void spawn(World world, BlockPos pos, Block block, int count, boolean silk_torch) {
		if (!silk_torch) {
			int xp = 0;
			Random r = new Random();
			ImmutableList<Integer> xpRange = CppChainMap.getExperienceRange(block);
			for (int i = 0; i < count; i++) {
				if (xpRange != null) {
					xp += r.nextInt(xpRange.get(1) - xpRange.get(0) + 1) + xpRange.get(0);
				}
			}
			ExperienceOrbEntity xpOrb = new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), xp);
			world.spawnEntity(xpOrb);
		}
		Item breakResult = CppChainMap.getBreakResult(block);
		while (count > 0) {
			ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(breakResult == null || silk_torch ? block.asItem() : breakResult, Math.min(count, 64)));
			itemEntity.setToDefaultPickupDelay();
			world.spawnEntity(itemEntity);
			count -= 64;
		}
	}

	public static boolean canChain(ItemStack toolStack, BlockState blockState) {
		// TODO
		return toolStack.isSuitableFor(blockState);
	}
}
