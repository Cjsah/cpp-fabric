package net.cpp.gui.screen;

import net.cpp.gui.handler.AMachineScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public abstract class AMachineScreen<T extends AMachineScreenHandler> extends HandledScreen<T> {
	public AMachineScreen(T handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}
}
