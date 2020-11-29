package net.cpp.blockentity;

import net.cpp.gui.handler.CraftingMachineScreenHandler;
import net.cpp.init.CppBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

public class CraftingMachineBlockEntity extends LootableContainerBlockEntity implements Tickable {

    private DefaultedList<ItemStack> inventory;

    public CraftingMachineBlockEntity() {
        super(CppBlockEntities.CRAFTING_MACHINE);
        this.inventory = DefaultedList.ofSize(10, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        // Crafting List
        System.out.println("run");
        if (inventory.get(1).getItem() == Items.IRON_INGOT) {
            if (inventory.get(9).isEmpty()) {
                inventory.set(9, new ItemStack(Items.DIAMOND, 1));
                inventory.get(1).decrement(1);
            }else if (inventory.get(9).getItem() == Items.DIAMOND) {
                inventory.get(9).increment(1);
                inventory.get(1).decrement(1);
            }
        }
    }

    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(tag)) {
            Inventories.fromTag(tag, this.inventory);
        }
    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        if (!this.serializeLootTable(tag)) {
            Inventories.toTag(tag, this.inventory);
        }

        return tag;
    }


    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.cpp.crafting_machine");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new CraftingMachineScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public int size() {
        return 10;
    }
}
