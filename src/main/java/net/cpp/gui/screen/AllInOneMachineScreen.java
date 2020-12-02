package net.cpp.gui.screen;

import static net.cpp.gui.handler.SlotTool.*;
import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.gui.handler.AllInOneMachineScreenHandler;
import net.cpp.gui.handler.CraftingMachineScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AllInOneMachineScreen extends HandledScreen<AllInOneMachineScreenHandler> {
	public static final Identifier TEXTURE = new Identifier("cpp:textures/gui/all_in_one_machine.png");
	public static final Identifier XP = new Identifier("cpp:textures/gui/xp.png");
	private boolean narrow;
	public final OutputDirectionButton oButton = new OutputDirectionButton(buttonWidget -> {
		this.client.interactionManager.clickButton(this.handler.syncId, 1010);
	}, handler.blockEntity.propertyDelegate);
	public final TexturedButtonWidget temperatureButton = new TexturedButtonWidget(0, 0, 16, 16, 0, 0, 0, TEXTURE,
			buttonWidget -> {
				handler.blockEntity.shiftTemperature();
				this.client.interactionManager.clickButton(this.handler.syncId, 1011);
			}) {
		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			minecraftClient.getTextureManager().bindTexture(TEXTURE);
			RenderSystem.enableDepthTest();
			drawTexture(matrices, x, y, (handler.blockEntity.propertyDelegate.get(3) / 0x10) * 16,
					isHovered() ? 166 + 16 : 166, 16, 16);
			if (this.isHovered()) {
				this.renderToolTip(matrices, mouseX, mouseY);
			}
		}
	};
	public final TexturedButtonWidget pressureButton = new TexturedButtonWidget(0, 0, 16, 16, 0, 0, 0, TEXTURE,
			buttonWidget -> {
				handler.blockEntity.shiftPressure();

				this.client.interactionManager.clickButton(this.handler.syncId, 1012);
			}) {
		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			minecraftClient.getTextureManager().bindTexture(TEXTURE);
			RenderSystem.enableDepthTest();
			drawTexture(matrices, x, y, (handler.blockEntity.propertyDelegate.get(3) % 0x10) * 16,
					isHovered() ? 198 + 16 : 198, 16, 16);
			if (this.isHovered()) {
				this.renderToolTip(matrices, mouseX, mouseY);
			}
		}
	};

	public AllInOneMachineScreen(AllInOneMachineScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	protected void init() {
		super.init();
		narrow = this.width < 379;
		temperatureButton.setPos(x + x(1), y + y(0));
		addButton(temperatureButton);
		pressureButton.setPos(x + x(1), y + y(1));
		addButton(pressureButton);
		oButton.setPos(x + x(1), y + y(2));
		addButton(oButton);
		titleX = 29;
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
}
