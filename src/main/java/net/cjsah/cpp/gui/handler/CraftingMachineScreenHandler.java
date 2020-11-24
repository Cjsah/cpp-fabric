package net.cjsah.cpp.gui.handler;

import net.cjsah.cpp.blockentity.CraftingMachineBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class CraftingMachineScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    public CraftingMachineBlockEntity block;

    public CraftingMachineScreenHandler(int syncId, PlayerInventory playerInventory, CraftingMachineBlockEntity block) {
        this(syncId, playerInventory, new SimpleInventory(10), block);
    }

    public CraftingMachineScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, CraftingMachineBlockEntity block) {
        super(null, syncId);
        this.inventory = inventory;
        this.block = block;
        checkSize(inventory, 10);
        inventory.onOpen(playerInventory.player);

        int m;
        int l;
        for(m = 0; m < 3; ++m) {
            for(l = 0; l < 3; ++l) {
                this.addSlot(new Slot(this.inventory, l + m * 3, 30 + l * 18, 17 + m * 18));
            }
        }

        this.addSlot(new Slot(this.inventory, 9, 124, 35) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });

        for(m = 0; m < 3; ++m) {
            for(l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }

        for(m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < this.inventory.size()) {
                if (!this.insertItem(itemStack2, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return itemStack;
    }
}
