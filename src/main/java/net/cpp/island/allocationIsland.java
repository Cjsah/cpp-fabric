package net.cpp.island;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 回字形分配岛屿
 */
public class allocationIsland {
    private int islandID = 1;
    // 圈数
    private int around = 1;


    public void allocation(World world, PlayerEntity player) {
        BlockPos pos = getPos();
        world.setBlockState(pos, Blocks.BEDROCK.getDefaultState());
        world.setBlockState(pos.add(0, 1, 0), Blocks.CHEST.getDefaultState());
        LootableContainerBlockEntity blockEntity = (LootableContainerBlockEntity) world.getBlockEntity(pos.add(0, 1, 0));
        assert blockEntity != null;
        blockEntity.setStack(0, new ItemStack(Items.OAK_SAPLING, 4));
        blockEntity.setStack(1, new ItemStack(Items.DIRT, 1));
        blockEntity.setStack(2, new ItemStack(Items.BONE_MEAL, 16));

        // player设为已拥有岛屿

        player.teleport(pos.getX() + .5D, pos.getY() + 3D, pos.getZ() + .5D);

    }

    private BlockPos getPos() {
        if (this.islandID > getCount(this.around)) this.around++;

        int aroundId = this.islandID - getCount(this.around - 1);
        int length = this.around * 2 + 1;

        this.islandID++;

        if (aroundId <= length) {
            return createPos(-this.around, aroundId - this.around - 1);
        }else if (aroundId <= length * 2 - 2) {
            return createPos(aroundId - length - this.around, this.around);
        }else if (aroundId <= length * 3 - 2) {
            return createPos(this.around, length - aroundId + this.around * 3);
        }else {
            return createPos(length - aroundId + this.around * 5, -this.around);
        }
    }

    private int getCount(int around) {
        return around * (around + 1) / 2 * 8;
    }

    private BlockPos createPos(int x, int z) {
        return new BlockPos(x * 1000, 62, z * 1000);
    }

}
