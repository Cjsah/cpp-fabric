package net.cpp.gui.screen;

import net.cpp.api.CodingTool;
import net.cpp.gui.handler.ItemProcessorScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemProcessorScreen extends AOutputMachineScreen<ItemProcessorScreenHandler> {
	public static final Identifier BACKGROUND = new Identifier("cpp:textures/gui/item_processor.png");

	public ItemProcessorScreen(ItemProcessorScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}
	@Override
	protected Identifier getBackground() {
		return BACKGROUND;
	}

	@Override
	protected void init() {
		super.init();
		oButton.setPos(x + CodingTool.x(4), y + CodingTool.y(2));
	}
}
