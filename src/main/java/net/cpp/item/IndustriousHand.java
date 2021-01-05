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
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;

import net.cpp.api.CodingTool;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.Block;
import net.minecraft.block.KelpPlantBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ScheduledTick;
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
			int type = check(block);
			switch (type) {
			case 0:
				if (!BoneMealItem.useOnFertilizable(boneMeals.get(0).getStack(), world, pos2)) {
					if (block instanceof SweetBerryBushBlock) {
						((SweetBerryBushBlock) block).onUse(world.getBlockState(pos2), world, pos2, null, Hand.MAIN_HAND, new BlockHitResult(itemFrameEntity.getPos().add(0, 2, 0), Direction.DOWN, pos2, false));
						tick(itemFrameEntity);
					} else {
						List<ItemStack> droppeds = Block.getDroppedStacks(world.getBlockState(pos2), (ServerWorld) world, pos2, world.getBlockEntity(pos2));
						world.breakBlock(pos2, false, itemFrameEntity);
						if (block instanceof BambooBlock || block instanceof KelpPlantBlock) {
							BlockPos pos3 = pos2.up();
							while (world.getBlockState(pos3).isOf(block)) {
								world.breakBlock(pos3, false, itemFrameEntity);
								pos3 = pos3.up();
							}
						}
						boolean has = false;
						for (ItemStack stack : droppeds) {
							if (stack.isOf(block.asItem())) {
								stack.decrement(1);
								has = true;
								break;
							}
						}
						CodingTool.drop((ServerWorld) world, itemFrameEntity.getPos(), droppeds);
						if (has) {
							world.setBlockState(pos2, block.getDefaultState());
							tick(itemFrameEntity);
						}
					}
				}
				if (world.getBlockState(pos2.up()).isOf(block)) {
					world.breakBlock(pos2.up(), true);
				}
				return true;
			case 1:
				List<ItemEntity> shearses = world.getEntitiesByClass(ItemEntity.class, new Box(pos2, pos2).expand(2), itemEntity -> itemEntity.getStack().isOf(Items.SHEARS));
				if (!shearses.isEmpty()) {
					boneMeals.get(0).getStack().decrement(1);
					Block.dropStacks(world.getBlockState(pos2), world, pos2, world.getBlockEntity(pos2), itemFrameEntity, shearses.get(0).getStack());
					shearses.get(0).getStack().damage(1, world.random, null);
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
