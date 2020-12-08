package net.cpp.gui.handler;

import net.cpp.block.entity.AMachineBlockEntity;
import net.cpp.gui.screen.OutputDirectionButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public abstract class AMachineScreenHandler extends ScreenHandler {
	protected PlayerInventory playerInventory;
	public final AMachineBlockEntity blockEntity;

	public AMachineScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory,
			AMachineBlockEntity blockEntity) {
		super(type, syncId);
		this.playerInventory = playerInventory;
		this.blockEntity = blockEntity;
		for (int m = 0; m < 9; ++m) {
			this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
		}
		for (int m = 0; m < 3; ++m) {
			for (int l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
			}
		}
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		if (blockEntity != null && id == OutputDirectionButton.SYNCHRONIZED_ID) {
			blockEntity.shiftOutputDir();
			return true;
		}
		return false;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return blockEntity.canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index >= 0 && index < 9) {
				if (!insertItem(itemStack2, 9, 36, true))
					return ItemStack.EMPTY;
			} else if (index >= 9 && index < 36)
				if (!insertItem(itemStack2, 0, 9, false))
					return ItemStack.EMPTY;
		}
		return itemStack;
	}
}
