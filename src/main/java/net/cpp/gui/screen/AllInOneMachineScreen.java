package net.cpp.gui.screen;

import static net.cpp.gui.handler.SlotTool.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.gui.handler.AllInOneMachineScreenHandler;
import net.cpp.gui.handler.CraftingMachineScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class AllInOneMachineScreen extends HandledScreen<AllInOneMachineScreenHandler> {
	public static final Identifier TEXTURE = new Identifier("cpp:textures/gui/all_in_one_machine.png");
	public static final Identifier XP = new Identifier("cpp:textures/gui/xp.png");
	public static final TranslatableText[] TEMPERATURE_TEXTS = { new TranslatableText("gui.high_temperature"),
			new TranslatableText("gui.ordinary_temperature"), new TranslatableText("gui.low_temperature") };
	public static final TranslatableText[] PRESSURE_TEXTS = { new TranslatableText("gui.high_pressure"),
			new TranslatableText("gui.ordinary_pressure"), new TranslatableText("gui.low_pressure") };
	public final List<Text> temperatureTooltip = Arrays.asList(TEMPERATURE_TEXTS[0],
			OutputDirectionButton.CLICK_TO_SHIFT);
	public final List<Text> pressureTooltip = Arrays.asList(PRESSURE_TEXTS[0], OutputDirectionButton.CLICK_TO_SHIFT);
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
		temperatureTooltip.set(1, OutputDirectionButton.CLICK_TO_SHIFT);
		pressureTooltip.set(1, OutputDirectionButton.CLICK_TO_SHIFT);
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

		if (handler.blockEntity.isWorking()) {
//			System.out.println(handler.blockEntity.getWorkTimeTotal());
			this.drawTexture(matrices, x + 74, y + 36, 176, 0, 11,
					handler.blockEntity.getWorkTimeTotal() > 0
							? 1 + 15 * handler.blockEntity.getWorkTime() / handler.blockEntity.getWorkTimeTotal()
							: 0);
		}
		int exp = handler.blockEntity.getExpStorage();
		if (exp > 0) {
			client.getTextureManager().bindTexture(XP);
			int t = (int) (System.currentTimeMillis() % (16 * 50) / 50);
//			System.out.println(t);
			drawTexture(matrices, x + 152, y + 68 - (exp + 1) / 2, 0, t * 50, 16, (exp + 1) / 2, 16, 800);
		}
		client.getTextureManager().bindTexture(TEXTURE);
		drawTexture(matrices, x + 152, y + 18, 176, 18, 16, 50);
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
		} else if (temperatureButton.isHovered()) {
			temperatureTooltip.set(0, TEMPERATURE_TEXTS[handler.blockEntity.getTemperature().ordinal()]);
			renderTooltip(matrices, temperatureTooltip, x, y);
		} else if (pressureButton.isHovered()) {
			pressureTooltip.set(0, PRESSURE_TEXTS[handler.blockEntity.getPressure().ordinal()]);
			renderTooltip(matrices, pressureTooltip, x, y);
		}
		super.drawMouseoverTooltip(matrices, x, y);
	}
}
