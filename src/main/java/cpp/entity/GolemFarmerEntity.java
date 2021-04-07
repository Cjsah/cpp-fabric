package cpp.entity;

import static net.minecraft.block.Blocks.BAMBOO;
import static net.minecraft.block.Blocks.BROWN_MUSHROOM_BLOCK;
import static net.minecraft.block.Blocks.MELON;
import static net.minecraft.block.Blocks.MUSHROOM_STEM;
import static net.minecraft.block.Blocks.NETHER_WART_BLOCK;
import static net.minecraft.block.Blocks.PUMPKIN;
import static net.minecraft.block.Blocks.RED_MUSHROOM_BLOCK;
import static net.minecraft.block.Blocks.SHROOMLIGHT;
import static net.minecraft.block.Blocks.SUGAR_CANE;
import static net.minecraft.block.Blocks.WARPED_WART_BLOCK;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import cpp.api.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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
			List<ItemStack> droppeds = new LinkedList<>();
			if (block instanceof CropBlock) {
				CropBlock cropBlock = (CropBlock) block;
				if (cropBlock.isMature(state)) {
					Utils.excavate((ServerWorld) world, this, getBlockPos(), droppeds);
					listMerge(droppeds);
					if (decrement(block.asItem()))
						world.setBlockState(blockPos, block.getDefaultState());
				}
			} else if ((block instanceof SweetBerryBushBlock || block instanceof NetherWartBlock) && state.get(Properties.AGE_3) >= 3) {
				Utils.excavate((ServerWorld) world, this, getBlockPos(), droppeds);
				droppeds.get(0).decrement(1);
				world.setBlockState(blockPos, block.getDefaultState());
				listMerge(droppeds);
			} else if (LOG_TO_SAPLING.containsKey(block)) {
				Utils.excavate((ServerWorld) world, this, blockPos, droppeds);
				listMerge(droppeds);
				Block sapling = LOG_TO_SAPLING.get(block);
				if (sapling.canPlaceAt(sapling.getDefaultState(), world, blockPos) && decrement(sapling.asItem()))
					world.setBlockState(blockPos, sapling.getDefaultState());
			} else if (HARVESTABLE.contains(block)) {
				Utils.excavate((ServerWorld) world, this, blockPos, droppeds);
				listMerge(droppeds);
			}
			experience= Utils.mend(mainHandStack, experience);
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
