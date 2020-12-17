package net.cpp.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.gui.handler.PortableCraftingMachineScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class PortableCraftingMachineScreen extends HandledScreen<PortableCraftingMachineScreenHandler> {
	public PortableCraftingMachineScreen(PortableCraftingMachineScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(CraftingMachineScreen.BACKGROUND);
		int i = this.x;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}
}
