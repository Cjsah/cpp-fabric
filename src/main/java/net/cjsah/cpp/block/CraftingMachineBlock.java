package net.cjsah.cpp.block;

import net.cjsah.cpp.CraftingppMod;
import net.cjsah.cpp.blockentity.CraftingMachineBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CraftingMachineBlock extends Block implements BlockEntityProvider {

    public CraftingMachineBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new CraftingMachineBlockEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CraftingMachineBlockEntity) {
                player.openHandledScreen((CraftingMachineBlockEntity)blockEntity);
//                player.incrementStat(CraftingppMod.INSPECT_CRAFTING_MACHINE);
            }

            return ActionResult.CONSUME;
        }
    }
}
