package net.cpp.screen;

import net.cpp.init.CppBlocks;
import net.cpp.screen.handler.EmptyBookshelfScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EmptyBookshelfScreen extends AMachineScreen<EmptyBookshelfScreenHandler> {
	public static final Identifier BACKGROUND = getBackgroundByName(Registry.BLOCK.getId(CppBlocks.EMPTY_BOOKSHELF).getPath());
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
