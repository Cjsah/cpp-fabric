package net.cjsah.cpp.blockentity;

import net.cjsah.cpp.gui.handler.CraftingMachineScreenHandler;
import net.cjsah.cpp.init.CppBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

import java.util.Iterator;

public class CraftingMachineBlockEntity extends LockableContainerBlockEntity implements Tickable {

    private DefaultedList<ItemStack> inventory;
    private byte OUT = 1;
    public CompoundTag tag;

    public CraftingMachineBlockEntity() {
        super(CppBlockEntities.CRAFTING_MACHINE);
        this.inventory = DefaultedList.ofSize(10, ItemStack.EMPTY);
    }

    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        this.tag = tag;
        Inventories.fromTag(tag, this.inventory);
        if (tag.contains("out")) {
            OUT = tag.getByte("out");
        }
    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        this.tag = tag;
        Inventories.toTag(tag, this.inventory);
        tag.putByte("out", OUT);
        return tag;
    }

    public byte changeFacing() {
        if (OUT == 6) {
            return OUT = 1;
        }
        return ++OUT;
    }

    public byte getFacing() {
        return OUT;
    }















    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.cpp.crafting_machine");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new CraftingMachineScreenHandler(syncId, playerInventory, this, this);
    }

    @Override
    public int size() {
        return 10;
    }

    @Override
    public boolean isEmpty() {
        Iterator var1 = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack)var1.next();
        } while(itemStack.isEmpty());

        return false;

    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ItemStack itemStack = (ItemStack)this.inventory.get(slot);
        boolean bl = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack) && ItemStack.areTagsEqual(stack, itemStack);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void tick() {

    }

    @Override
    public void clear() {
        this.inventory.clear();
    }
}
