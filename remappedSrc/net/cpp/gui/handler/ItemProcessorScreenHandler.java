package net.cpp.gui.handler;

import net.cpp.api.CodingTool;
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
		this(syncId, playerInventory, new ItemProcessorBlockEntity(playerInventory.player.getBlockPos(), playerInventory.player.getBlockState()));
	}

	public ItemProcessorScreenHandler(int syncId, PlayerInventory playerInventory, ItemProcessorBlockEntity blockEntity) {
		super(CppScreenHandler.ITEM_PROCESSOR, syncId, playerInventory, blockEntity);
		this.blockEntity = blockEntity;

		addSlot(new Slot(blockEntity, 0, CodingTool.x(3), CodingTool.y(0)));
		addSlot(new Slot(blockEntity, 1, CodingTool.x(3), CodingTool.y(1)));
		addSlot(new ResultSlot(blockEntity, 2, CodingTool.x(6), CodingTool.y(1)));
		addSlot(new ResultSlot(blockEntity, 3, CodingTool.x(7), CodingTool.y(1)));
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return ((LootableContainerBlockEntity) blockEntity).canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
//			itemStack = itemStack2.copy();
			if (index >= 36 && index < blockEntity.size()) {
				this.insertItem(itemStack2, 0, 36, true);
//				slot.onStackChanged(itemStack2, itemStack);
			} else {
				if (!(ItemProcessorBlockEntity.RECIPES.containsKey(itemStack2.getItem()) && insertItem(itemStack2, 36, 37, false) || insertItem(itemStack2, 37, 38, false))) {
					super.transferSlot(player, index);
				}
			}
		}
		return ItemStack.EMPTY;
	}
}
