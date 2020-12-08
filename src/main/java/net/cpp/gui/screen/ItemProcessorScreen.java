package net.cpp.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.gui.handler.ItemProcessorScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import static net.cpp.gui.handler.SlotTool.*;

public class ItemProcessorScreen extends HandledScreen<ItemProcessorScreenHandler> {
	public static final Identifier BACKGROUND = new Identifier("cpp:textures/gui/item_processor.png");
	public final OutputDirectionButton oButton = new OutputDirectionButton(buttonWidget -> {
		this.client.interactionManager.clickButton(this.handler.syncId, OutputDirectionButton.SYNCHRONIZED_ID);
	}, handler.blockEntity.propertyDelegate);

	public ItemProcessorScreen(ItemProcessorScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(BACKGROUND);
		int i = this.x;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
		// TODO 自动生成的方法存根

	}

	@Override
	protected void init() {
		super.init();
		oButton.setPos(x + x(4), y + y(2));
		this.addButton(oButton);
		this.titleX = 29;
	}

	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		if (oButton.isHovered()) {
			renderTooltip(matrices, oButton.getTooltip(), x, y);
		}
		super.drawMouseoverTooltip(matrices, x, y);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
}
