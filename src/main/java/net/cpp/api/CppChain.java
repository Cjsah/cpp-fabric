package net.cpp.api;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

public class CppChain {
    public static void chain(World world, ServerPlayerEntity player, BlockPos pos, Block block) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (world.getBlockState(pos.add(i,j,k)).getBlock() == block) {
                        if (player.interactionManager.getGameMode().isCreative()) {
                            world.breakBlock(pos.add(i,j,k), false, player);
                        }else {
                            ListTag enchant = player.getMainHandStack().getEnchantments();
                            boolean silk_torch = false;
                            boolean fortune = false;
                            short fortune_level = 0;
                            for (Tag tag : enchant) {
                                if (Objects.equals(((CompoundTag) tag).getString("id"), "minecraft:silk_touch")) {
                                    silk_torch = true;
                                }else if (Objects.equals(((CompoundTag) tag).getString("id"), "minecraft:fortune")) {
                                    fortune = true;
                                    fortune_level = ((CompoundTag) tag).getShort("lvl");
                                }
                            }
                            if (silk_torch) {
                                world.breakBlock(pos.add(i,j,k), false, player);
                                spawn(world, pos, block, 1);
                            }else if (fortune && fortune_level > 0) {
                                int value = new Random().nextInt(fortune_level+1) + 1;
                                world.breakBlock(pos.add(i,j,k), true, player);
                                spawn(world, pos, Items.EMERALD, value > 2 ? value - 2 : 0);
                            }else {
                                world.breakBlock(pos.add(i,j,k), true, player);
                            }
                        }
                        chain(world, player, pos.add(i,j,k), block);
                    }
                }
            }
        }
    }
    private static void spawn(World world, BlockPos pos, @Nullable Item item, int count) {
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item, count));
        itemEntity.setToDefaultPickupDelay();
        world.spawnEntity(itemEntity);
    }
    private static void spawn(World world, BlockPos pos, Block block, int count) {
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(block, count));
        itemEntity.setToDefaultPickupDelay();
        world.spawnEntity(itemEntity);
    }
}
