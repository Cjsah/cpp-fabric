package net.cpp.gui.handler;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class PortableCraftingMachineScreenHandler extends PortableCraftingTableScreenHandler {

	public PortableCraftingMachineScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(syncId, playerInventory);
	}

	public PortableCraftingMachineScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(syncId, playerInventory, context);
	}

}
