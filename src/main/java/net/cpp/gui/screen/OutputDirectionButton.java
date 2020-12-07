package net.cpp.gui.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class OutputDirectionButton extends TexturedButtonWidget {
	public static final Identifier TEXTURE = new Identifier("cpp:textures/gui/output_direction_button.png");
	public static final TranslatableText[] DIRECTION_TEXT = { new TranslatableText("gui.output_to_east"),
			new TranslatableText("gui.output_to_west"), new TranslatableText("gui.output_to_up"),
			new TranslatableText("gui.output_to_down"), new TranslatableText("gui.output_to_south"),
			new TranslatableText("gui.output_to_west") };
	public static final TranslatableText CLICK_TO_SHIFT = new TranslatableText("gui.click_to_shift");
	public final PropertyDelegate propertyDelegate;
	public final List<Text> tooltipTexts = Arrays.asList(DIRECTION_TEXT[0],CLICK_TO_SHIFT);

	public OutputDirectionButton(ButtonWidget.PressAction pressAction, PropertyDelegate propertyDelegate) {
		super(0, 0, 16, 16, 0, 0, 0, TEXTURE, pressAction);
		this.propertyDelegate = propertyDelegate;
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		minecraftClient.getTextureManager().bindTexture(TEXTURE);
		RenderSystem.enableDepthTest();
		drawTexture(matrices, x, y, propertyDelegate.get(0) * 16, isHovered() ? 16 : 0, 16, 16, 96, 32);
		if (this.isHovered()) {
			this.renderToolTip(matrices, mouseX, mouseY);
		}
	}

	@Override
	public void onPress() {
		propertyDelegate.set(0, propertyDelegate.get(0) + 1);
		super.onPress();
	}
	public List<Text> getTooltip() {
		tooltipTexts.set(0, DIRECTION_TEXT[propertyDelegate.get(0)]);
		tooltipTexts.set(1, CLICK_TO_SHIFT);
		return tooltipTexts;
	}
}