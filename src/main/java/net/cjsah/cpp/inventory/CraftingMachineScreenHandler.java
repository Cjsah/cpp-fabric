package net.cjsah.cpp.inventory;

import net.cjsah.cpp.CraftingppMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;

public class CraftingMachineScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    public static final Identifier IDENTIFIER = new Identifier("cpp", "cpp.container");

    public CraftingMachineScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(CraftingppMod.CRAFTING_MACHINE_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        checkSize(inventory, 10);
        inventory.onOpen(playerInventory.player);

        int c = 0;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(inventory, c, 30 + i * 18, 17 + j * 18));
                c++;
            }
        }
        this.addSlot(new Slot(inventory, c, 124, 35));
        c++;
        // Player inventory
        for (int n = 0; n < 3; ++n) {
            for (int m = 0; m < 9; ++m) {
                this.addSlot(new Slot(inventory, c, 8 + m * 18, 84 + n * 18));
                c++;
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, c, 8 + i * 18, 142));
            c++;
        }

        this.inventory.onOpen(playerInventory.player);

    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            final ItemStack stack2 = slot.getStack();
            stack = stack2.copy();

            if (index < this.inventory.size()) {
                if (!this.insertItem(stack2, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(stack2, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (stack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return stack;
    }
    @Override
    public ItemStack onSlotClick(int i, int j, SlotActionType type, PlayerEntity player) {
        if (type != SlotActionType.CLONE) {
            if (i >= 0 && player.inventory.selectedSlot + 27 + this.inventory.size() == i) {
                return ItemStack.EMPTY;
            }
        }

        return super.onSlotClick(i, j, type, player);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.inventory.onClose(player);
    }

    public CraftingMachineInventory inventory() {
        return (CraftingMachineInventory) this.inventory;
    }
}
