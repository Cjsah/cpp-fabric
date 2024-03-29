package cpp.screen;

import static cpp.api.Utils.nextSyncId;
import static cpp.api.Utils.x;
import static cpp.api.Utils.y;

import com.mojang.blaze3d.systems.RenderSystem;

import cpp.init.CppBlocks;
import cpp.screen.handler.TradeMachineScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TradeMachineScreen extends AExpMachineScreen<TradeMachineScreenHandler> {
	public static final Identifier BACKGROUND = getBackgroundByName(Registry.BLOCK.getId(CppBlocks.TRADE_MACHINE).getPath());
	public static final int MODE_BUTTON_SYNC_ID = nextSyncId();
	/**
	 * 交易模式按钮，点击切换模式
	 */
	public final TexturedButtonWidget modeButton = new TexturedButtonWidget(0, 0, 16, 16, 0, 0, 0, BACKGROUND, buttonWidget -> client.interactionManager.clickButton(this.handler.syncId, MODE_BUTTON_SYNC_ID)) {
		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			RenderSystem.setShaderTexture(0, BACKGROUND);
			RenderSystem.enableDepthTest();
			drawTexture(matrices, x, y, handler.blockEntity.getMode() * 16, 166 + (isHovered() ? 16 : 0), 16, 16);
		}
	};

	public TradeMachineScreen(TradeMachineScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected Identifier getBackground() {
		return BACKGROUND;
	}

	@Override
	public boolean workTimeIsHovered(int mx, int my) {
		return false;
	}

	@Override
	protected void init() {
		super.init();
		oButton.setPos(x + x(3), y + y(2));
		modeButton.setPos(x + x(3), y + y(0));
		addDrawableChild(modeButton);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		super.drawBackground(matrices, delta, mouseX, mouseY);
		drawTexture(matrices, x + 57, y + 35, 32 + handler.blockEntity.getMode() * 27, 166, 27, 17);
	}
}
