package net.cpp.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.gui.handler.AOutputMachineScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public abstract class AOutputMachineScreen<T extends AOutputMachineScreenHandler> extends HandledScreen<T> {
	public final OutputDirectionButton oButton = new OutputDirectionButton(buttonWidget -> {
		client.interactionManager.clickButton(this.handler.syncId, OutputDirectionButton.SYNC_ID);
	}, handler.blockEntity);
	public static final TranslatableText CLICK_TO_SHIFT = new TranslatableText("gui.click_to_shift");
	public AOutputMachineScreen(T handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
	@Override
	protected void init() {
		super.init();
		addButton(oButton);
	}
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(getBackground());
		int i = this.x;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}

	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		if (oButton.isHovered())
			renderTooltip(matrices, oButton.getTooltip(), x, y);
		super.drawMouseoverTooltip(matrices, x, y);
	}
	protected abstract Identifier getBackground();
}
