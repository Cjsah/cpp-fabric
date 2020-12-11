package net.cpp.gui.screen;

import static net.cpp.gui.handler.SlotTool.x;
import static net.cpp.gui.handler.SlotTool.y;

import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.gui.handler.AllInOneMachineScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class AllInOneMachineScreen extends HandledScreen<AllInOneMachineScreenHandler> {
	public static final Identifier BACKGROUND = new Identifier("cpp:textures/gui/all_in_one_machine.png");
	public static final Identifier XP = new Identifier("cpp:textures/gui/xp.png");
	public static final Identifier FRAME = new Identifier("cpp:textures/gui/frame.png");
	public static final TranslatableText[] TEMPERATURE_TEXTS = { new TranslatableText("gui.ordinary_temperature"),
			new TranslatableText("gui.low_temperature"), new TranslatableText("gui.high_temperature") };
	public static final TranslatableText[] PRESSURE_TEXTS = { new TranslatableText("gui.ordinary_pressure"),
			new TranslatableText("gui.low_pressure"), new TranslatableText("gui.high_pressure") };
	public final List<Text> temperatureTooltip = Arrays.asList(TEMPERATURE_TEXTS[0],
			OutputDirectionButton.CLICK_TO_SHIFT);
	public final List<Text> pressureTooltip = Arrays.asList(PRESSURE_TEXTS[0], OutputDirectionButton.CLICK_TO_SHIFT);
	public final OutputDirectionButton oButton = new OutputDirectionButton(buttonWidget -> {
		this.client.interactionManager.clickButton(this.handler.syncId, OutputDirectionButton.SYNCHRONIZED_ID);
	}, handler.blockEntity.propertyDelegate);
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
			drawTexture(matrices, x, y, (handler.blockEntity.propertyDelegate.get(3) / 0x10) * 16,
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
		temperatureButton.setPos(x + x(1), y + y(0));
		addButton(temperatureButton);
		pressureButton.setPos(x + x(1), y + y(1));
		addButton(pressureButton);
		oButton.setPos(x + x(1), y + y(2));
		addButton(oButton);
		titleX = 29;
	}

	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(BACKGROUND);
		int i = this.x;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

		if (handler.blockEntity.isWorking()) {
			this.drawTexture(matrices, x + 74, y + 36, 176, 0, 11,
					handler.blockEntity.getWorkTimeTotal() > 0
							? 1 + 15 * handler.blockEntity.getWorkTime() / handler.blockEntity.getWorkTimeTotal()
							: 0);
		}
		int exp = handler.blockEntity.getExpStorage();
		if (exp > 0) {
			client.getTextureManager().bindTexture(XP);
			int t = (int) (System.currentTimeMillis() % (16 * 50) / 50);
			drawTexture(matrices, x + 152, y + 68 - (exp + 1) / 2, 0, t * 50, 16, (exp + 1) / 2, 16, 800);
		}
		client.getTextureManager().bindTexture(FRAME);
		drawTexture(matrices, x + 151, y + 17, 0, 0, 18, 52, 18, 52);
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
		} else if (xpIsHovered(x, y)) {
			renderTooltip(matrices, new LiteralText(String.format("%d/100", handler.blockEntity.getExpStorage())), x,
					y);
		} else if (workTimeIsHovered(x, y)) {
			renderTooltip(matrices, new LiteralText(String.format("%.1fs/%.1fs", handler.blockEntity.getWorkTime() / 20.,
					handler.blockEntity.getWorkTimeTotal() / 20.)), x, y);
		}
		super.drawMouseoverTooltip(matrices, x, y);
	}

	public boolean xpIsHovered(int mx, int my) {
		return mx >= x + 151 && mx <= x + 151 + 18 && my >= y + 17 && my <= y + 17 + 52;
	}

	public boolean workTimeIsHovered(int mx, int my) {
		return mx >= x + 74 && mx <= x + 74 + 11 && my >= y + 36 && my <= y + 36 + 16;
	}
}
