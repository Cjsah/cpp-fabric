package net.cpp.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.gui.handler.AMachineScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class AMachineScreen<T extends AMachineScreenHandler> extends HandledScreen<T> {
	public AMachineScreen(T handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(getBackground());
		int i = this.x;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}
	protected abstract Identifier getBackground();
}
