package cpp.screen;

import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import cpp.api.Utils;
import cpp.block.entity.AOutputMachineBlockEntity;
import cpp.block.entity.IOutputDiractional;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class OutputDirectionButton extends TexturedButtonWidget {
	public static final int SYNC_ID = Utils.nextSyncId();
	public static final Identifier TEXTURE = AMachineScreen.getBackgroundByName("output_direction_button");
	/**
	 * 悬浮文本
	 */
	public static final TranslatableText[] DIRECTION_TEXT = { new TranslatableText("gui.output_to_east"),
			new TranslatableText("gui.output_to_west"), new TranslatableText("gui.output_to_up"),
			new TranslatableText("gui.output_to_down"), new TranslatableText("gui.output_to_south"),
			new TranslatableText("gui.output_to_north") };
	private final List<Text> tooltipTexts = Arrays.asList(DIRECTION_TEXT[0], AOutputMachineScreen.CLICK_TO_SHIFT);
	private final AOutputMachineBlockEntity blockEntity;

	public OutputDirectionButton(ButtonWidget.PressAction pressAction, AOutputMachineBlockEntity blockEntity) {
		super(0, 0, 16, 16, 0, 0, 0, TEXTURE, 16, 16, pressAction, (button, matrices, mouseX, mouseY) -> {
			
		}, LiteralText.EMPTY);
		this.blockEntity = blockEntity;
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.enableDepthTest();
		drawTexture(matrices, x, y, IOutputDiractional.dirToByte(blockEntity.getOutputDir()) * 16, isHovered() ? 16 : 0,
				16, 16, 96, 32);
	}

	@Override
	public void onPress() {
		blockEntity.shiftOutputDir();
		super.onPress();
	}

	public List<Text> getTooltip() {
		tooltipTexts.set(0, DIRECTION_TEXT[IOutputDiractional.dirToByte(blockEntity.getOutputDir())]);
		tooltipTexts.set(1, AOutputMachineScreen.CLICK_TO_SHIFT);
		return tooltipTexts;
	}
}