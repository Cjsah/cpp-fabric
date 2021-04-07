package cpp.misc;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.GourdBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import static net.minecraft.block.Blocks.*;

public class ChainProcessor {
	/**
	 * 键：物品<br>
	 * 值：可连锁的方块
	 */
	protected static final Map<Item, Set<Block>> TOOL_TO_BLOCKS = new HashMap<>();
	/**
	 * 偏移量，用于遍历一个方块周围的26个方块
	 */
	protected static final List<BlockPos> OFFSETS = BlockPos.stream(-1, -1, -1, 1, 1, 1).map(BlockPos::toImmutable).filter(pos -> !BlockPos.ORIGIN.equals(pos)).collect(Collectors.toList());
	public final ServerWorld world;
	public final BlockPos startPos;
	public final Block block;
	public final ItemStack toolStack;
	public final ServerPlayerEntity player;
	/**
	 * 掉落物
	 * 
	 * @see #excavate(BlockPos)
	 */
	protected final List<ItemStack> droppeds = new LinkedList<>();
	/**
	 * 待挖掘的方块坐标
	 */
	protected final Queue<BlockPos> posQueue;

	public ChainProcessor(ServerWorld startWorld, BlockPos startPos, Block block, ItemStack toolStack, ServerPlayerEntity player) {
		this.world = startWorld;
		this.startPos = startPos;
		this.block = block;
		this.toolStack = toolStack;
		this.player = player;
		posQueue = new PriorityQueue<>(Comparator.comparingDouble(a -> a.getSquaredDistance(startPos)));
		posQueue.add(startPos);
	}

	/**
	 * 开始挖掘
	 */
	public void start() {
		double squaredDis = 0;
		Set<BlockPos> set = new HashSet<>();
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
				if (blockState.isOf(block) && checkRipe(blockState) && !set.contains(pos)) {
					posQueue.offer(pos);
					set.add(pos);
					squaredDis = Math.max(squaredDis, startPos.getSquaredDistance(pos));
				}
			}
		}
		for (ExperienceOrbEntity orb : world.getEntitiesByClass(ExperienceOrbEntity.class, new Box(startPos, startPos).expand(Math.sqrt(squaredDis)), orb -> true)) {
			orb.teleport(startPos.getX(), startPos.getY(), startPos.getZ());
		}

		for (ItemStack stack : droppeds) {
			Block.dropStack(world, startPos, stack);
		}
	}

	/**
	 * 挖掘方块，不直接掉落物品，不产生粒子和音效，战利品存入{@link #droppeds}
	 * 
	 * @param pos
	 */
	protected void excavate(BlockPos pos) {
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

	/**
	 * 查询是否能连锁
	 * 
	 * @param item       工具物品
	 * @param blockState 被挖掘的方块
	 * @return 能连锁
	 */
	public static boolean canChain(Item item, BlockState blockState) {
		Set<Block> set = TOOL_TO_BLOCKS.get(item);
		if (set == null || !set.contains(blockState.getBlock()))
			return false;
		return checkRipe(blockState);
	}

	/**
	 * 判断一个状态方块是否是成熟的作物
	 * 
	 * @param blockState 状态方块
	 * @return 是成熟的作物或不是作物
	 */
	public static boolean checkRipe(BlockState blockState) {
		return (!(blockState.getBlock() instanceof CropBlock) || ((CropBlock) blockState.getBlock()).isMature(blockState)) && (!(blockState.getBlock() instanceof NetherWartBlock) || blockState.get(NetherWartBlock.AGE) >= 3);
	}

	static {
		Set<Block> axeBlocks = new HashSet<>(), pickaxeBlocks = new HashSet<>(), shovelBlocks, hoeBlocks;
		for (Entry<RegistryKey<Block>, Block> entry : Registry.BLOCK.getEntries()) {
			String s = entry.getKey().getValue().getPath();
			if (s.contains("_log") || s.contains("_stem"))
				axeBlocks.add(entry.getValue());
			else if (s.contains("_ore"))
				pickaxeBlocks.add(entry.getValue());
			else if (entry.getValue() instanceof CropBlock)
				axeBlocks.add(entry.getValue());
			else if (entry.getValue() instanceof GourdBlock)
				axeBlocks.add(entry.getValue());
		}

		axeBlocks.addAll(ImmutableSet.of(NETHER_WART));
		pickaxeBlocks.addAll(ImmutableSet.of(ANCIENT_DEBRIS, OBSIDIAN));
		shovelBlocks = new HashSet<>(ImmutableSet.of(SAND, RED_SAND, GRAVEL, CLAY));
		hoeBlocks = new HashSet<>(ImmutableSet.of(HAY_BLOCK, NETHER_WART_BLOCK, WARPED_WART_BLOCK, SHROOMLIGHT));

		for (Item item : FabricToolTags.AXES.values())
			TOOL_TO_BLOCKS.put(item, axeBlocks);
		for (Item item : FabricToolTags.PICKAXES.values())
			TOOL_TO_BLOCKS.put(item, pickaxeBlocks);
		for (Item item : FabricToolTags.SHOVELS.values())
			TOOL_TO_BLOCKS.put(item, shovelBlocks);
		for (Item item : FabricToolTags.HOES.values())
			TOOL_TO_BLOCKS.put(item, hoeBlocks);
	}
}
