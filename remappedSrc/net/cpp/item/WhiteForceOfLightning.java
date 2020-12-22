package net.cpp.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class WhiteForceOfLightning extends Item {

    private static final Map<Block, Block> exchange = new HashMap<>();

    public WhiteForceOfLightning(Settings settings) {
        super(settings);
        this.register();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack item = user.getStackInHand(hand);
        if (!world.isClient) {
            BlockPos pos = new BlockPos(user.getPos()).add(0, -1, 0);
            BlockState blockState = world.getBlockState(pos);
            Block block = exchange.get(blockState.getBlock());
            int value = 0;
            if (block != null) {
                for (int i = -7; i <= 7; i++) {
                    for (int j = -7; j <= 7; j++) {
                        BlockPos tmpPos = pos.add(i, 0, j);

                        if (world.getBlockState(tmpPos).getBlock() == blockState.getBlock()) {
                            if (world.getBlockState(tmpPos).getBlock() instanceof FluidDrainable) {
                                ItemStack itemStack2 = ((FluidDrainable) blockState.getBlock()).tryDrainFluid(world, tmpPos, world.getBlockState(tmpPos));
                                System.out.println(itemStack2);
                                if (!itemStack2.isEmpty()) {
                                    world.setBlockState(tmpPos, block.getDefaultState());
                                    value++;
                                }
                            }else {
                                world.setBlockState(tmpPos, block.getDefaultState());
                                value++;
                            }
                        }
                    }
                }
                if (value > 0) {
                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                    return TypedActionResult.success(item);
                }
            }
        }
        return TypedActionResult.pass(item);
    }

    private void register() {
        exchange.put(Blocks.DIRT, Blocks.GRASS_BLOCK);
        exchange.put(Blocks.GRASS_BLOCK, Blocks.STONE);
        exchange.put(Blocks.STONE, Blocks.SAND);
        exchange.put(Blocks.SAND, Blocks.RED_SAND);
        exchange.put(Blocks.RED_SAND, Blocks.GRAVEL);
        exchange.put(Blocks.GRAVEL, Blocks.PODZOL);
        exchange.put(Blocks.PODZOL, Blocks.MYCELIUM);
        exchange.put(Blocks.MYCELIUM, Blocks.DIRT);

        exchange.put(Blocks.NETHERRACK, Blocks.CRIMSON_NYLIUM);
        exchange.put(Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM);
        exchange.put(Blocks.WARPED_NYLIUM, Blocks.BLACKSTONE);
        exchange.put(Blocks.BLACKSTONE, Blocks.END_STONE);
        exchange.put(Blocks.END_STONE, Blocks.NETHERRACK);

        exchange.put(Blocks.WATER, Blocks.ICE);
        exchange.put(Blocks.ICE, Blocks.WATER);

        exchange.put(Blocks.LAVA, Blocks.OBSIDIAN);
        exchange.put(Blocks.OBSIDIAN, Blocks.LAVA);
    }
}
