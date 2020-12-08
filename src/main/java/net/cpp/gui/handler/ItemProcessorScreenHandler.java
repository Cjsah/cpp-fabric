package net.cpp.gui.handler;

import static net.cpp.gui.handler.SlotTool.x;
import static net.cpp.gui.handler.SlotTool.y;

import net.cpp.block.entity.ItemProcessorBlockEntity;
import net.cpp.init.CppScreenHandler;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ItemProcessorScreenHandler extends AMachineScreenHandler {
	public final ItemProcessorBlockEntity blockEntity;

	public ItemProcessorScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new ItemProcessorBlockEntity());
	}

	public ItemProcessorScreenHandler(int syncId, PlayerInventory playerInventory,
			ItemProcessorBlockEntity blockEntity) {
		super(CppScreenHandler.ITEM_PROCESSOR, syncId, playerInventory, blockEntity);
		this.blockEntity = blockEntity;
		addProperties(blockEntity.propertyDelegate);

		addSlot(new Slot(blockEntity, 0, x(3), y(0)));
		addSlot(new Slot(blockEntity, 1, x(3), y(1)));
		addSlot(new ResultSlot(blockEntity, 2, x(6), y(1)));
		addSlot(new ResultSlot(blockEntity, 3, x(7), y(1)));
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return ((LootableContainerBlockEntity) blockEntity).canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index >= 36 && index < 41) {
				if (!this.insertItem(itemStack2, 0, 36, true))
					return ItemStack.EMPTY;
				slot.onStackChanged(itemStack2, itemStack);
			} else {
				if (slots.get(36).getStack().isEmpty()
						&& ItemProcessorBlockEntity.RECIPES.containsKey(itemStack2.getItem())
						&& insertItem(itemStack2, 36, 37, false) || insertItem(itemStack2, 37, 38, false)
						|| super.transferSlot(player, index) == ItemStack.EMPTY) {
					return ItemStack.EMPTY;
				}
			}
			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTakeItem(player, itemStack2);
		}
		return itemStack;
	}
}
