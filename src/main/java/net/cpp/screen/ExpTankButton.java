package net.cpp.screen;

import java.util.Arrays;
import java.util.List;

import net.cpp.api.Utils;
import net.cpp.block.entity.AExpMachineBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

/**
 * 经验槽（按钮）
 *
 * @author Ph-苯
 */
public class ExpTankButton extends TexturedButtonWidget {
	public static final int SYNC_ID = Utils.nextSyncId();
	public static final Identifier XP = AMachineScreen.getBackgroundByName("xp");
	public static final Identifier FRAME = AMachineScreen.getBackgroundByName("frame");
	public static final TranslatableText CLICK_TO_TAKE_OUT = new TranslatableText("gui.click_to_take_out");
	public final List<Text> tooltipTexts = Arrays.asList(null, CLICK_TO_TAKE_OUT);
	private final AExpMachineBlockEntity blockEntity;
	
	public ExpTankButton(ButtonWidget.PressAction pressAction, AExpMachineBlockEntity blockEntity) {
		super(0, 0, 18, 52, 0, 0, 0, FRAME, pressAction);
		this.blockEntity = blockEntity;
	}
	
	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		int percent = blockEntity.getExpStorage() * 100 / AExpMachineBlockEntity.XP_CAPACITY;
		if (percent > 0) {
			minecraftClient.getTextureManager().bindTexture(XP);
			int t = (int) (System.currentTimeMillis() % (16 * 50) / 50);
			drawTexture(matrices, x + 1, y + 51 - (percent + 1) / 2, 0, t * 50, 16, (percent + 1) / 2, 16, 800);
		}
		minecraftClient.getTextureManager().bindTexture(FRAME);
		drawTexture(matrices, x, y, 0, 0, 18, 52, 18, 52);
	}
	
	public List<Text> getTooltip() {
		tooltipTexts.set(0, new LiteralText(String.format("%d/%d", blockEntity.getExpStorage(), AExpMachineBlockEntity.XP_CAPACITY)));
		tooltipTexts.set(1, CLICK_TO_TAKE_OUT);
		return tooltipTexts;
	}
	
}
