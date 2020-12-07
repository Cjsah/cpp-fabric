package net.cpp.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.gui.handler.CraftingMachineScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CraftingMachineScreen extends HandledScreen<CraftingMachineScreenHandler> {
	private static final Identifier TEXTURE = new Identifier("cpp:textures/gui/crafting_machine.png");
	private boolean narrow;
	public final OutputDirectionButton oButton;

	public CraftingMachineScreen(CraftingMachineScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		oButton = new OutputDirectionButton(buttonWidget -> {
			client.interactionManager.clickButton(handler.syncId, 1010);
		}, handler.propertyDelegate);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	protected void init() {
		super.init();
		this.narrow = this.width < 379;
		oButton.setPos(x + 94, y + 57);
		this.addButton(oButton);
		this.titleX = 29;
	}

	public void tick() {
		super.tick();
	}

	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(TEXTURE);
		int i = this.x;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}

	protected boolean isPointWithinBounds(int xPosition, int yPosition, int width, int height, double pointX,
			double pointY) {
		return !this.narrow && super.isPointWithinBounds(xPosition, yPosition, width, height, pointX, pointY);
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return super.mouseClicked(mouseX, mouseY, button);
	}

	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
		return mouseX < (double) left || mouseY < (double) top || mouseX >= (double) (left + this.backgroundWidth)
				|| mouseY >= (double) (top + this.backgroundHeight);
	}

	protected void onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType) {
		super.onMouseClick(slot, invSlot, clickData, actionType);
	}

	public void removed() {
		super.removed();
	}

	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		if (oButton.isHovered()) {
			renderTooltip(matrices, oButton.getTooltip(), x, y);
		}
		super.drawMouseoverTooltip(matrices, x, y);
	}
}
