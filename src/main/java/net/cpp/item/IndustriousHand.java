package net.cpp.item;

import static net.minecraft.block.Blocks.BAMBOO;
import static net.minecraft.block.Blocks.BAMBOO_SAPLING;
import static net.minecraft.block.Blocks.BEETROOTS;
import static net.minecraft.block.Blocks.CARROTS;
import static net.minecraft.block.Blocks.COCOA;
import static net.minecraft.block.Blocks.FERN;
import static net.minecraft.block.Blocks.GRASS;
import static net.minecraft.block.Blocks.KELP;
import static net.minecraft.block.Blocks.KELP_PLANT;
import static net.minecraft.block.Blocks.LARGE_FERN;
import static net.minecraft.block.Blocks.LILAC;
import static net.minecraft.block.Blocks.PEONY;
import static net.minecraft.block.Blocks.POTATOES;
import static net.minecraft.block.Blocks.ROSE_BUSH;
import static net.minecraft.block.Blocks.SEAGRASS;
import static net.minecraft.block.Blocks.SUNFLOWER;
import static net.minecraft.block.Blocks.SWEET_BERRY_BUSH;
import static net.minecraft.block.Blocks.TALL_GRASS;
import static net.minecraft.block.Blocks.TALL_SEAGRASS;
import static net.minecraft.block.Blocks.TWISTING_VINES;
import static net.minecraft.block.Blocks.TWISTING_VINES_PLANT;
import static net.minecraft.block.Blocks.WEEPING_VINES;
import static net.minecraft.block.Blocks.WEEPING_VINES_PLANT;
import static net.minecraft.block.Blocks.WHEAT;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class IndustriousHand extends Item implements ITickableInItemFrame {
	public static final Set<Block> WITHOUT_SHEARS = ImmutableSet.of(WHEAT, BEETROOTS, CARROTS, POTATOES, BAMBOO, BAMBOO_SAPLING, SUNFLOWER, LILAC, ROSE_BUSH, PEONY, COCOA, SWEET_BERRY_BUSH, KELP, KELP_PLANT, TWISTING_VINES, TWISTING_VINES_PLANT, WEEPING_VINES, WEEPING_VINES_PLANT);
	public static final Set<Block> WITH_SHEARS = ImmutableSet.of(GRASS, FERN, SEAGRASS, TALL_GRASS, LARGE_FERN, TALL_SEAGRASS);

	public IndustriousHand(Settings settings) {
		super(settings);
	}

	public static int check(Block block) {
		if (WITHOUT_SHEARS.contains(block))
			return 0;
		if (WITH_SHEARS.contains(block))
			return 1;
		return -1;
	}

	@Override
	public boolean tick(ItemFrameEntity itemFrameEntity) {
		BlockPos pos2 = itemFrameEntity.getBlockPos().up(2);
		World world = itemFrameEntity.world;
		List<ItemEntity> boneMeals = world.getEntitiesByClass(ItemEntity.class, new Box(pos2, pos2).expand(2), itemEntity -> itemEntity.getStack().isOf(Items.BONE_MEAL));
		if (!boneMeals.isEmpty()) {
			Block block = world.getBlockState(pos2).getBlock();
			switch (check(block)) {
			case 0:
//				boneMeals.get(0).getStack().decrement(1);
				return BoneMealItem.useOnFertilizable(boneMeals.get(0).getStack(), world, pos2);
			case 1:
				List<ItemEntity> shearses = world.getEntitiesByClass(ItemEntity.class, new Box(pos2, pos2).expand(2), itemEntity -> itemEntity.getStack().isOf(Items.SHEARS));
				if (!shearses.isEmpty()) {
					boneMeals.get(0).getStack().decrement(1);
					shearses.get(0).getStack().damage(1, world.random, null);
					Block.dropStack(world, pos2, block.asItem().getDefaultStack());
					return true;
				}
				return false;
			default:
				return false;
			}
		}
		return false;
	}
}
