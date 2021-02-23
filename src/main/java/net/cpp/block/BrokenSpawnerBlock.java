package net.cpp.block;

import net.cpp.block.entity.BrokenSpawnerBlockEntity;
import net.cpp.init.CppBlockEntities;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BrokenSpawnerBlock extends BlockWithEntity {
    public BrokenSpawnerBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BrokenSpawnerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState ignore, BlockEntityType<T> type) {
        return checkType(type, CppBlockEntities.BROKEN_SPAWNER, world.isClient ? ((world1, pos, state, blockEntity) -> blockEntity.tick(world1, pos)) : null);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
