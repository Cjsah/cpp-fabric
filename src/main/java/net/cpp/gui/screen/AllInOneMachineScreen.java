package net.cpp.gui.screen;

import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.api.CodingTool;
import net.cpp.gui.handler.AllInOneMachineScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class AllInOneMachineScreen extends AExpMachineScreen<AllInOneMachineScreenHandler> {
	public static final Identifier BACKGROUND = new Identifier("cpp:textures/gui/all_in_one_machine.png");
	public static final TranslatableText[] TEMPERATURE_TEXTS = { new TranslatableText("gui.ordinary_temperature"),
			new TranslatableText("gui.low_temperature"), new TranslatableText("gui.high_temperature") };
	public static final TranslatableText[] PRESSURE_TEXTS = { new TranslatableText("gui.ordinary_pressure"),
			new TranslatableText("gui.low_pressure"), new TranslatableText("gui.high_pressure") };
	public final List<Text> temperatureTooltip = Arrays.asList(TEMPERATURE_TEXTS[0],
			OutputDirectionButton.CLICK_TO_SHIFT);
	public final List<Text> pressureTooltip = Arrays.asList(PRESSURE_TEXTS[0], OutputDirectionButton.CLICK_TO_SHIFT);
	public final TexturedButtonWidget temperatureButton = new TexturedButtonWidget(0, 0, 16, 16, 0, 0, 0, BACKGROUND,
			buttonWidget -> {
				handler.blockEntity.shiftTemperature();
				this.client.interactionManager.clickButton(this.handler.syncId, 1011);
			}) {
		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			minecraftClient.getTextureManager().bindTexture(BACKGROUND);
			RenderSystem.enableDepthTest();
			drawTexture(matrices, x, y, handler.blockEntity.getTemperature().ordinal() * 16,
					isHovered() ? 166 + 16 : 166, 16, 16);
			if (this.isHovered()) {
				this.renderToolTip(matrices, mouseX, mouseY);
			}
		}
	};
	public final TexturedButtonWidget pressureButton = new TexturedButtonWidget(0, 0, 16, 16, 0, 0, 0, BACKGROUND,
			buttonWidget -> {
				handler.blockEntity.shiftPressure();
				this.client.interactionManager.clickButton(this.handler.syncId, 1012);
			}) {
		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			minecraftClient.getTextureManager().bindTexture(BACKGROUND);
			RenderSystem.enableDepthTest();
			drawTexture(matrices, x, y, handler.blockEntity.getPressure().ordinal() * 16, isHovered() ? 198 + 16 : 198,
					16, 16);
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
	protected Identifier getBackground() {
		return BACKGROUND;
	}

	protected void init() {
		super.init();
		oButton.setPos(x + CodingTool.x(1), y + CodingTool.y(2));
		temperatureButton.setPos(x + CodingTool.x(1), y + CodingTool.y(0));
		addButton(temperatureButton);
		pressureButton.setPos(x + CodingTool.x(1), y + CodingTool.y(1));
		addButton(pressureButton);
	}

	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		super.drawBackground(matrices, delta, mouseX, mouseY);

		client.getTextureManager().bindTexture(getBackground());
		if (handler.blockEntity.isWorking()) {
			this.drawTexture(matrices, x + 74, y + 36, 176, 0, 11,
					handler.blockEntity.getWorkTimeTotal() > 0
							? 1 + 15 * handler.blockEntity.getWorkTime() / handler.blockEntity.getWorkTimeTotal()
							: 0);
		}
	}

	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		if (temperatureButton.isHovered()) {
			temperatureTooltip.set(0, TEMPERATURE_TEXTS[handler.blockEntity.getTemperature().ordinal()]);
			renderTooltip(matrices, temperatureTooltip, x, y);
		} else if (pressureButton.isHovered()) {
			pressureTooltip.set(0, PRESSURE_TEXTS[handler.blockEntity.getPressure().ordinal()]);
			renderTooltip(matrices, pressureTooltip, x, y);
		}
		super.drawMouseoverTooltip(matrices, x, y);
	}

	public boolean workTimeIsHovered(int mx, int my) {
		return mx >= x + 74 && mx <= x + 74 + 11 && my >= y + 36 && my <= y + 36 + 16;
	}
}
