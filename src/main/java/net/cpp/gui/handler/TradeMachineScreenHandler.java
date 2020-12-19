package net.cpp.gui.handler;

import static net.cpp.api.CodingTool.x;
import static net.cpp.api.CodingTool.y;

import net.cpp.api.CodingTool;
import net.cpp.block.entity.AllInOneMachineBlockEntity;
import net.cpp.block.entity.TradeMachineBlockEntity;
import net.cpp.gui.screen.TradeMachineScreen;
import net.cpp.init.CppItems;
import net.cpp.init.CppScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class TradeMachineScreenHandler extends AExpMachineScreenHandler {
	public final TradeMachineBlockEntity blockEntity;

	public TradeMachineScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new TradeMachineBlockEntity(playerInventory.player.getBlockPos(), playerInventory.player.getBlockState()));

	}

	public TradeMachineScreenHandler(int syncId, PlayerInventory playerInventory, TradeMachineBlockEntity blockEntity) {
		super(CppScreenHandler.TRADE_MACHINE, syncId, playerInventory, blockEntity);
		this.blockEntity = blockEntity;
		addSlot(new ExperienceBottleSlot(blockEntity, 0, CodingTool.x(7), CodingTool.y(0)));
		addSlot(new Slot(blockEntity, 1, x(1), y(1)));
		addSlot(new Slot(blockEntity, 2, x(5), y(1)));
		addSlot(new Slot(blockEntity, 3, x(6), y(1)));
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		if (id == TradeMachineScreen.MODE_BUTTON_SYNC_ID) {
			blockEntity.shiftMode();
			return true;
		}
		return super.onButtonClick(player, id);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack = slot.getStack();
			if (index >= 36 && index < 36 + blockEntity.getInvStackList().size()) {
				this.insertItem(itemStack, 0, 36, true);
			} else if (index < 36) {
				if (itemStack.getItem() == Items.EXPERIENCE_BOTTLE)
					insertItem(itemStack, 36, 37, false);
				else {
					if (blockEntity.getMode() == 0)
						insertItem(itemStack, 37, 38, false);
					else {
						if (TradeMachineBlockEntity.PLUGIN.contains(itemStack.getItem()))
							insertItem(itemStack, 38, 39, false);
						else if (itemStack.getItem() == Items.EMERALD || itemStack.getItem() == Items.GOLD_INGOT || itemStack.getItem() == CppItems.MOON_SHARD)
							insertItem(itemStack, 39, 40, false);
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}
}
