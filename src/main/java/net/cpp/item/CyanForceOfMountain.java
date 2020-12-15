package net.cpp.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Clearable;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;

public class CyanForceOfMountain extends Item {
    public CyanForceOfMountain(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {

            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    BlockPos pos = new BlockPos(user.getPos().add(i, -1, j));

                    if (world.getBlockState(pos).getBlock() == Blocks.AIR) {
                        world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                    }
                }

            }

            for (int i = (int) user.getPos().y; i <=255; i++) {
                for (int j = -2; j <= 2; j++) {
                    for (int k = -2; k <= 2; k++) {
                        BlockPos pos = new BlockPos(user.getPos().add(j, i - (int)user.getPos().y, k));
                        if (world.getBlockState(pos).getBlock() != Blocks.AIR) {
                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
        }

        return super.use(world, user, hand);
    }
}
