package net.cpp.gui.handler;

import net.cpp.block.entity.ItemProcessorBlockEntity;
import net.cpp.init.CppScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class ItemProcessorScreenHandler extends ScreenHandler {
	private PlayerInventory playerInventory;
	private ItemProcessorBlockEntity blockEntity;

	public ItemProcessorScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(CppScreenHandler.ITEM_PROCESSOR, syncId);
		this.playerInventory = playerInventory;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		// TODO 自动生成的方法存根
		return blockEntity.canPlayerUse(player);
	}

}
