package net.cpp.item;

import static net.minecraft.block.Blocks.*;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class IndustriousHand extends Item implements ITickableInItemFrame {
	public static final Set<Block> WITHOUT_SHEARS = ImmutableSet.of(WHEAT, BEETROOTS, CARROTS, POTATOES, BAMBOO, BAMBOO_SAPLING, SUNFLOWER, LILAC, ROSE_BUSH, PEONY, COCOA, SWEET_BERRY_BUSH, KELP, KELP_PLANT, TWISTING_VINES, TWISTING_VINES_PLANT, WEEPING_VINES, WEEPING_VINES_PLANT);
	public static final Set<Block> WITH_SHEARS = ImmutableSet.of(GRASS, FERN, SEAGRASS, TALL_GRASS, LARGE_FERN, TALL_SEAGRASS);

	public IndustriousHand(Settings settings) {
		super(settings);
	}

	@Override
	public boolean tickInItemFrame(ServerWorld world, Vec3d pos, ItemStack stack) {
		BlockPos pos2 = new BlockPos(pos).up(2);
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

	public static int check(Block block) {
		if (WITHOUT_SHEARS.contains(block))
			return 0;
		if (WITH_SHEARS.contains(block))
			return 1;
		return -1;
	}
}
