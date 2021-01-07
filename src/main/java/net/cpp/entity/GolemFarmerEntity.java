package net.cpp.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import static net.minecraft.block.Blocks.*;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;

import net.cpp.api.CodingTool;
import net.minecraft.block.BeetrootsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class GolemFarmerEntity extends AMovingGolemEntity {
	public static final Map<Block, Block> LOG_TO_SAPLING = new HashMap<>();
	public static final Set<Block> HARVESTABLE = new HashSet<>();

	public GolemFarmerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void work() {
		if (!world.isClient) {
			BlockState state = getBlockState();
			Block block = state.getBlock();
			BlockPos blockPos = getBlockPos();
			List<ItemStack> droppeds = new LinkedList<ItemStack>();
			if (block instanceof CropBlock) {
				CropBlock cropBlock = (CropBlock) block;
				if (cropBlock.isMature(state)) {
					CodingTool.excavate((ServerWorld) world, this, getBlockPos(), droppeds);
					listMerge(droppeds);
					if (decrement(block.asItem()))
						world.setBlockState(blockPos, block.getDefaultState());
				}
			} else if (block instanceof SweetBerryBushBlock && state.get(SweetBerryBushBlock.AGE) >= 3) {
				CodingTool.excavate((ServerWorld) world, this, getBlockPos(), droppeds);
				droppeds.get(0).decrement(1);
				world.setBlockState(blockPos, block.getDefaultState());
				listMerge(droppeds);
			} else if (LOG_TO_SAPLING.containsKey(block)) {
				CodingTool.excavate((ServerWorld) world, this, blockPos, droppeds);
				listMerge(droppeds);
				Block sapling = LOG_TO_SAPLING.get(block);
				if (decrement(sapling.asItem()))
					world.setBlockState(blockPos, sapling.getDefaultState());
			} else if (HARVESTABLE.contains(block)) {
				CodingTool.excavate((ServerWorld) world, this, blockPos, droppeds);
				listMerge(droppeds);
			}
		}
	}

	public static BlockState getHarvestedState(BlockState state) {

		return Blocks.AIR.getDefaultState();
	}

	public boolean decrement(Item item) {
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (stack.isOf(item)) {
				stack.decrement(1);
				return true;
			}
		}
		return false;
	}

	static {
		for (Entry<RegistryKey<Block>, Block> entry : Registry.BLOCK.getEntries()) {
			Identifier id1 = entry.getKey().getValue();
			String path = id1.getPath();
			if (path.contains("_log")) {
				String sapling = path.replace("_log", "_sapling");
				Block block = Registry.BLOCK.get(new Identifier(id1.getNamespace(), sapling));
				if (block != Blocks.AIR) {
					LOG_TO_SAPLING.put(entry.getValue(), block);
				}
			} else if (path.contains("_stem")) {
				String sapling = path.replace("_stem", "_fungus");
				Block block = Registry.BLOCK.get(new Identifier(id1.getNamespace(), sapling));
				if (block != Blocks.AIR) {
					LOG_TO_SAPLING.put(entry.getValue(), block);
				}
			} else if (path.contains("_leaves")) {
				HARVESTABLE.add(entry.getValue());
			}
		}
		HARVESTABLE.addAll(ImmutableSet.of(PUMPKIN, MELON, MUSHROOM_STEM, RED_MUSHROOM_BLOCK, BROWN_MUSHROOM_BLOCK, SUGAR_CANE, BAMBOO, SHROOMLIGHT, NETHER_WART_BLOCK, WARPED_WART_BLOCK));
	}
}
