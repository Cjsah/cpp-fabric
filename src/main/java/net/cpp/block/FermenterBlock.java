package net.cpp.block;

import net.cpp.api.CodingTool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;

public class FermenterBlock extends ComposterBlock {

    public FermenterBlock(Settings settings) {
        super(settings);
    }
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        if (state.get(LEVEL) == 8) {
            SidedInventory superInventory = super.getInventory(state,world,pos);
            class FullFermenterInventory extends SimpleInventory implements SidedInventory {
                private boolean dirty;
                public FullFermenterInventory() {
                    super(new ItemStack(Items.DIRT, world.getRandom().nextInt(3)+1));
                    System.out.println(getStack(0));

                }
                @Override
                public int[] getAvailableSlots(Direction side) {
                    return side == Direction.DOWN ? new int[] {0} : new int[0];
                }

                @Override
                public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
                    return false;
                }

                @Override
                public boolean canExtract(int slot, ItemStack stack, Direction dir) {
//                    System.out.println(getStack(0));
                    return !dirty && dir == Direction.DOWN && !getStack(0).isEmpty();
                }

                public void markDirty() {
                    if (getStack(0).isEmpty()) {
                        dirty=true;
                        superInventory.markDirty();
                    }
                }
            }
            return new FullFermenterInventory();
        }
        return super.getInventory(state,world,pos);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(LEVEL)==8){
            ActionResult actionResult = super.onUse(state, world, pos, player, hand, hit);
            for(ItemEntity itemEntity: world.getEntitiesByClass(ItemEntity.class,new Box(pos).expand(0,1,0), itemEntity -> itemEntity.getStack().isOf(Items.BONE_MEAL)&&itemEntity.getStack().getCount()==1&&itemEntity.getAge()==0)) {
                itemEntity.setStack(new ItemStack(Items.DIRT, world.getRandom().nextInt(3)+1));
                break;
            }
            return actionResult;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
