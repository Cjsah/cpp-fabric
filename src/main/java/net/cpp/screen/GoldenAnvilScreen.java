package net.cpp.screen;

import static net.cpp.api.Utils.x;
import static net.cpp.api.Utils.y;

import net.cpp.init.CppBlocks;
import net.cpp.screen.handler.GoldenAnvilScreenHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GoldenAnvilScreen extends AExpMachineScreen<GoldenAnvilScreenHandler> {
	public static final Identifier BACKGROUND = getBackgroundByName(Registry.BLOCK.getId(CppBlocks.GOLDEN_ANVIL).getPath());

	public GoldenAnvilScreen(GoldenAnvilScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected Identifier getBackground() {
		return BACKGROUND;
	}

	@Override
	public boolean workTimeIsHovered(int mx, int my) {
		return false;
	}

	@Override
	protected void init() {
		super.init();
		oButton.setPos(x + x(5), y + y(2));
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		super.drawBackground(matrices, delta, mouseX, mouseY);
	}
}
