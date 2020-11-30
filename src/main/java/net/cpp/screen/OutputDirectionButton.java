package net.cpp.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.Registeror;
import net.cpp.block.entity.IOutputDiractionalBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.Identifier;

public class OutputDirectionButton extends TexturedButtonWidget {
	public static final Identifier TEXTURE = new Identifier(Registeror.MOD_ID, "textures/gui/output_direction_button.png");
	public PropertyDelegate propertyDelegate;

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

//	@Override
//	public void onClick(double mouseX, double mouseY) {
//		super.onClick(mouseX, mouseY);
//		outputDiractional.shiftOutputDir();
//	}
}