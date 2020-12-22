package net.cpp.gui.handler;

import net.cpp.api.CodingTool;
import net.cpp.block.entity.AllInOneMachineBlockEntity;
import net.cpp.block.entity.EmptyBookshelfBlockEntity;
import net.cpp.init.CppScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class EmptyBookshelfScreenHandler extends AMachineScreenHandler {
	public final EmptyBookshelfBlockEntity blockEntity;

	public EmptyBookshelfScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new EmptyBookshelfBlockEntity(playerInventory.player.getBlockPos(), playerInventory.player.getBlockState()));
	}

	public EmptyBookshelfScreenHandler(int syncId, PlayerInventory playerInventory, EmptyBookshelfBlockEntity blockEntity) {
		super(CppScreenHandler.EMPTY_BOOKSHELF, syncId, playerInventory, blockEntity);
		this.blockEntity = blockEntity;
		for (int i = 0; i < 3; i++)
			addSlot(new BookshelfSlot(blockEntity, i, CodingTool.x(3 + i), CodingTool.y(1)));
	}

	public static class BookshelfSlot extends Slot {
		public BookshelfSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return EmptyBookshelfBlockEntity.STORABLE.contains(stack.getItem());
		}
	}
}
