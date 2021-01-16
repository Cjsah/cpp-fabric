package net.cpp.screen;

import static net.cpp.api.CodingTool.x;
import static net.cpp.api.CodingTool.y;

import net.cpp.screen.handler.GoldenAnvilScreenHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GoldenAnvilScreen extends AExpMachineScreen<GoldenAnvilScreenHandler> {
	public static final Identifier BACKGROUND = new Identifier("cpp:textures/gui/golden_anvil.png");

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
