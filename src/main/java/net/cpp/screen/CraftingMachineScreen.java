package net.cpp.screen;

import net.cpp.screen.handler.CraftingMachineScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CraftingMachineScreen extends AOutputMachineScreen<CraftingMachineScreenHandler> {
	public static final Identifier BACKGROUND = new Identifier("cpp:textures/gui/crafting_machine.png");

	public CraftingMachineScreen(CraftingMachineScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected Identifier getBackground() {
		return BACKGROUND;
	}

	protected void init() {
		super.init();
		oButton.setPos(x + 94, y + 57);
	}
}
