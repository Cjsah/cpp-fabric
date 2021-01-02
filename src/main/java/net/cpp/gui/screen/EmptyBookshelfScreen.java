package net.cpp.gui.screen;

import net.cpp.gui.handler.EmptyBookshelfScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class EmptyBookshelfScreen extends AMachineScreen<EmptyBookshelfScreenHandler> {
	public static final Identifier BACKGROUND = new Identifier("cpp:textures/gui/empty_bookshelf.png");
	public EmptyBookshelfScreen(EmptyBookshelfScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}
	@Override
	protected Identifier getBackground() {
		return BACKGROUND;
	}
	@Override
	public void onClose() {
		super.onClose();
	}
}
