package net.cpp.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import static net.cpp.api.CodingTool.*;
import net.cpp.gui.handler.AllInOneMachineScreenHandler;
import net.cpp.gui.handler.TradeMachineScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TradeMachineScreen extends AExpMachineScreen<TradeMachineScreenHandler> {
	public static final Identifier BACKGROUND = new Identifier("cpp:textures/gui/trade_machine.png");
	public static final int MODE_BUTTON_SYNC_ID = nextSyncId();
	public final TexturedButtonWidget modeButton = new TexturedButtonWidget(0, 0, 16, 16, 0, 0, 0, BACKGROUND, buttonWidget -> {
		client.interactionManager.clickButton(this.handler.syncId, MODE_BUTTON_SYNC_ID);
	}) {
		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(BACKGROUND);
			RenderSystem.enableDepthTest();
			drawTexture(matrices, x, y, handler.blockEntity.getMode() * 16, 166 + (isHovered() ? 16 : 0), 16, 16);
		}
	};

	public TradeMachineScreen(TradeMachineScreenHandler handler, PlayerInventory inventory, Text title) {
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
		oButton.setPos(x + x(3), y + y(2));
		modeButton.setPos(x + x(3), y + y(0));
		addButton(modeButton);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		super.drawBackground(matrices, delta, mouseX, mouseY);
		drawTexture(matrices, x + 57, y + 35, 32 + handler.blockEntity.getMode() * 27, 166, 27, 17);
	}
}
