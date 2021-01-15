package net.cpp.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class PortableCraftingTableScreenHandler extends CraftingScreenHandler {

	public PortableCraftingTableScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(syncId, playerInventory);
	}

	public PortableCraftingTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(syncId, playerInventory, context);
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}
