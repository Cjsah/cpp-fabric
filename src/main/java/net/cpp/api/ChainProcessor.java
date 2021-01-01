package net.cpp.api;

import static net.minecraft.block.Blocks.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;

import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class ChainProcessor {
	private static final Map<Item, Set<Block>> TOOL_TO_BLOCKS = new HashMap<>();
	private static final Set<Block> RIPE = ImmutableSet.of(WHEAT, BEETROOTS, CARROTS, POTATOES, NETHER_WART);

	private static final List<BlockPos> OFFSETS = BlockPos.stream(-1, -1, -1, 1, 1, 1).map(BlockPos::toImmutable).filter(pos -> !BlockPos.ORIGIN.equals(pos)).collect(Collectors.toList());
	public final ServerWorld world;
	public final BlockPos startPos;
	public final Block block;
	public final ItemStack toolStack;
	public final ServerPlayerEntity player;
	private final List<ItemStack> droppeds = new LinkedList<>();
	private final Queue<BlockPos> posQueue = new LinkedList<>();

	public ChainProcessor(ServerWorld startWorld, BlockPos startPos, Block block, ItemStack toolStack, ServerPlayerEntity player) {
		this.world = startWorld;
		this.startPos = startPos;
		this.block = block;
		this.toolStack = toolStack;
		this.player = player;
		posQueue.add(startPos);
	}

	public void start() {
		double squaredDis = 0;
		int maxCount = toolStack.getMaxDamage() * (EnchantmentHelper.getLevel(Enchantments.UNBREAKING, toolStack) + 1);
		while (!posQueue.isEmpty() && toolStack.getDamage() + 1 < toolStack.getMaxDamage() && maxCount > 0) {
			BlockPos pos0 = posQueue.poll();
			if (!world.getBlockState(pos0).isOf(block)) {
				continue;
			}
			excavate(pos0);
			maxCount--;
			for (BlockPos offset : OFFSETS) {
				BlockPos pos = pos0.add(offset);
				BlockState blockState = world.getBlockState(pos);
				if (blockState.isOf(block) && (!RIPE.contains(block) || ((CropBlock) block).isMature(blockState))) {
					posQueue.offer(pos);
					squaredDis = Math.max(squaredDis, startPos.getSquaredDistance(pos));
				}
			}
		}
		for (ExperienceOrbEntity orb : world.getEntitiesByClass(ExperienceOrbEntity.class, new Box(startPos, startPos).expand(Math.sqrt(squaredDis)), orb -> true)) {
			orb.teleport(startPos.getX(), startPos.getY(), startPos.getZ());
		}
		System.out.println(droppeds.size());
		for (ItemStack stack : droppeds) {
			Block.dropStack(world, startPos, stack);
		}
	}

	private void excavate(BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		block.onBreak(world, pos, blockState, player);
		block.onBroken(world, pos, blockState);
		if (!player.isCreative()) {
			block.onStacksDropped(blockState, world, pos, toolStack);
			droppeds.addAll(Block.getDroppedStacks(blockState, world, pos, blockEntity, player, toolStack));
			player.incrementStat(Stats.MINED.getOrCreateStat(block));
			toolStack.postMine(world, blockState, pos, player);
		}
		world.removeBlock(pos, false);
	}

	public static boolean canChain(Item item, BlockState blockState) {
		Set<Block> set = TOOL_TO_BLOCKS.get(item);
		if (set == null || !set.contains(blockState.getBlock()))
			return false;
		return !RIPE.contains(blockState.getBlock()) || ((CropBlock) blockState.getBlock()).isMature(blockState);
	}

	static {
		Set<Block> tmpSet = new HashSet<>();
		for (Entry<RegistryKey<Block>, Block> entry : Registry.BLOCK.getEntries()) {
			String s = entry.getKey().getValue().getPath();
			if (s.contains("_log") || s.contains("_stem")) {
				tmpSet.add(entry.getValue());
			}
		}
		tmpSet.addAll(ImmutableSet.of(MELON, PUMPKIN, WHEAT, BEETROOTS, CARROTS, POTATOES, NETHER_WART));
		for (Item item : FabricToolTags.AXES.values()) {
			TOOL_TO_BLOCKS.put(item, tmpSet);
		}

		tmpSet = new HashSet<>();
		for (Entry<RegistryKey<Block>, Block> entry : Registry.BLOCK.getEntries()) {
			String s = entry.getKey().getValue().getPath();
			if (s.contains("_ore")) {
				tmpSet.add(entry.getValue());
			}
		}
		tmpSet.addAll(ImmutableSet.of(ANCIENT_DEBRIS, OBSIDIAN));
		for (Item item : FabricToolTags.PICKAXES.values()) {
			TOOL_TO_BLOCKS.put(item, tmpSet);
		}

		tmpSet = new HashSet<>();
		tmpSet.addAll(ImmutableSet.of(SAND, RED_SAND, GRAVEL, CLAY));
		for (Item item : FabricToolTags.SHOVELS.values()) {
			TOOL_TO_BLOCKS.put(item, tmpSet);
		}

		tmpSet = new HashSet<>();
		tmpSet.addAll(ImmutableSet.of(HAY_BLOCK, NETHER_WART_BLOCK, WARPED_WART_BLOCK, SHROOMLIGHT));
		for (Item item : FabricToolTags.HOES.values()) {
			TOOL_TO_BLOCKS.put(item, tmpSet);
		}
	}
}
