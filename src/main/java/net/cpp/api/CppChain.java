package net.cpp.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CppChain {
    public static void chain(World world, PlayerEntity player, BlockPos pos, Block block) {
        BlockPos tmpPos;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    tmpPos = pos;
                    if (world.getBlockState(tmpPos.add(i,j,k)).getBlock() == block) {
                        world.breakBlock(tmpPos.add(i,j,k), true, player);
                        chain(world, player, pos.add(i,j,k), block);
                    }
                }
            }
        }
    }
}
