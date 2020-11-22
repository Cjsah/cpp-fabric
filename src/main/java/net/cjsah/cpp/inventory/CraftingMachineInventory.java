package net.cjsah.cpp.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

public class CraftingMachineInventory implements Inventory {

    private final DefaultedList<ItemStack> list;
    private final ItemStack container;

    public CraftingMachineInventory(ItemStack container) {
        this.list = DefaultedList.ofSize(10, ItemStack.EMPTY);
        this.container = container;
    }

    @Override
    public int size() {
        return 10;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.list) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.list.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.list, slot,amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        final ItemStack stack = this.list.remove(slot);
        this.setStack(slot,ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        list.set(slot, stack);
    }

    @Override
    public void markDirty() {
        this.write();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.list.size(); i++) {
            this.removeStack(i);
        }
    }

    public void write() {
        if (this.container != null && !this.container.isEmpty()) {
            this.container.getOrCreateTag().put("MachineContent", Inventories.toTag(new CompoundTag(), this.list, true));
        }
    }

}
