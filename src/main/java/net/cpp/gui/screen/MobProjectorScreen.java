package net.cpp.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.gui.handler.MobProjectorScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MobProjectorScreen extends HandledScreen<MobProjectorScreenHandler> {
	public static final Identifier BACKGROUND = new Identifier("cpp:textures/gui/item_projector_machine.png");

	public MobProjectorScreen(MobProjectorScreenHandler handler, PlayerInventory inventory, Text title) {
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
			client.getTextureManager().bindTexture(AllInOneMachineScreen.XP);
			int t = (int) (System.currentTimeMillis() % (16 * 50) / 50);
			drawTexture(matrices, x + 152, y + 68 - (exp + 1) / 2, 0, t * 50, 16, (exp + 1) / 2, 16, 800);
		}
		client.getTextureManager().bindTexture(AllInOneMachineScreen.FRAME);
		drawTexture(matrices, x + 151, y + 17, 0, 0, 18, 52, 18, 52);
	}

	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		super.drawMouseoverTooltip(matrices, x, y);
	}
}
