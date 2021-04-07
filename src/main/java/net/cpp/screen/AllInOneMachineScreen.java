package net.cpp.screen;

import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.api.Utils;
import net.cpp.init.CppBlocks;
import net.cpp.screen.handler.AllInOneMachineScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AllInOneMachineScreen extends AExpMachineScreen<AllInOneMachineScreenHandler> {
	public static final Identifier BACKGROUND = getBackgroundByName(Registry.BLOCK.getId(CppBlocks.ALL_IN_ONE_MACHINE).getPath());
	public static final TranslatableText[] TEMPERATURE_TEXTS = {new TranslatableText("gui.ordinary_temperature"), new TranslatableText("gui.low_temperature"), new TranslatableText("gui.high_temperature")};
	public static final TranslatableText[] PRESSURE_TEXTS = {new TranslatableText("gui.ordinary_pressure"), new TranslatableText("gui.low_pressure"), new TranslatableText("gui.high_pressure")};
	public static final int TEMPERATURE_BUTTON_SYNC_ID = Utils.nextSyncId(), PRESSURE_BUTTON_SYNC_ID = Utils.nextSyncId();
	public final List<Text> temperatureTooltip = Arrays.asList(TEMPERATURE_TEXTS[0], AOutputMachineScreen.CLICK_TO_SHIFT);
	public final List<Text> pressureTooltip = Arrays.asList(PRESSURE_TEXTS[0], AOutputMachineScreen.CLICK_TO_SHIFT);
	public final TexturedButtonWidget temperatureButton = new TexturedButtonWidget(0, 0, 16, 16, 0, 0, 0, BACKGROUND, buttonWidget -> {
		handler.blockEntity.shiftTemperature();
		client.interactionManager.clickButton(handler.syncId, TEMPERATURE_BUTTON_SYNC_ID);
	}) {
		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(BACKGROUND);
			RenderSystem.enableDepthTest();
			drawTexture(matrices, x, y, handler.blockEntity.getTemperature().ordinal() * 16, isHovered() ? 166 + 16 : 166, 16, 16);
			if (this.isHovered()) {
				this.renderToolTip(matrices, mouseX, mouseY);
			}
		}
	};
	public final TexturedButtonWidget pressureButton = new TexturedButtonWidget(0, 0, 16, 16, 0, 0, 0, BACKGROUND, buttonWidget -> {
		handler.blockEntity.shiftPressure();
		this.client.interactionManager.clickButton(this.handler.syncId, PRESSURE_BUTTON_SYNC_ID);
	}) {
		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(BACKGROUND);
			RenderSystem.enableDepthTest();
			drawTexture(matrices, x, y, handler.blockEntity.getPressure().ordinal() * 16, isHovered() ? 198 + 16 : 198, 16, 16);
			if (this.isHovered()) {
				this.renderToolTip(matrices, mouseX, mouseY);
			}
		}
	};
	
	public AllInOneMachineScreen(AllInOneMachineScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		temperatureTooltip.set(1, AOutputMachineScreen.CLICK_TO_SHIFT);
		pressureTooltip.set(1, AOutputMachineScreen.CLICK_TO_SHIFT);
	}
	
	@Override
	protected Identifier getBackground() {
		return BACKGROUND;
	}
	
	protected void init() {
		super.init();
		oButton.setPos(x + Utils.x(1), y + Utils.y(2));
		temperatureButton.setPos(x + Utils.x(1), y + Utils.y(0));
		addButton(temperatureButton);
		pressureButton.setPos(x + Utils.x(1), y + Utils.y(1));
		addButton(pressureButton);
	}
	
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		super.drawBackground(matrices, delta, mouseX, mouseY);
		
		client.getTextureManager().bindTexture(getBackground());
		if (handler.blockEntity.isWorking()) {
			this.drawTexture(matrices, x + 74, y + 36, 176, 0, 11, handler.blockEntity.getWorkTimeTotal() > 0 ? 1 + 15 * handler.blockEntity.getWorkTime() / handler.blockEntity.getWorkTimeTotal() : 0);
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
