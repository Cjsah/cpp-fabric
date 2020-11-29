package net.cpp.block;

import net.cpp.blockentity.CraftingMachineBlockEntity;
import net.fabricmc.fabric.impl.container.ContainerProviderImpl;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CraftingMachineBlock extends Block implements BlockEntityProvider {

    public CraftingMachineBlock() {
        super(Settings.of(Material.WOOD).strength(3.0F, 4.8F).sounds(BlockSoundGroup.WOOD));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CraftingMachineBlockEntity) {
                ((CraftingMachineBlockEntity)blockEntity).setCustomName(itemStack.getName());
            }
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new CraftingMachineBlockEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {

            ContainerProviderImpl.INSTANCE.openContainer(new Identifier("cpp", "crafting_machine"),player,buf -> buf.writeBlockPos(pos));

        }
        return ActionResult.SUCCESS;
    }
}
