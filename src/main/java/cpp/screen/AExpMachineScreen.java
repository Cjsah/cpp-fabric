package cpp.screen;

import cpp.screen.handler.AExpMachineScreenHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public abstract class AExpMachineScreen<T extends AExpMachineScreenHandler> extends AOutputMachineScreen<T> {
	public final ExpTankButton expTankButton = new ExpTankButton(buttonWidget -> client.interactionManager.clickButton(handler.syncId, ExpTankButton.SYNC_ID), handler.blockEntity);

	public AExpMachineScreen(T handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init() {
		super.init();
		expTankButton.setPos(x + 151, y + 17);
		addButton(expTankButton);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		super.drawBackground(matrices, delta, mouseX, mouseY);

//		int percent = handler.blockEntity.getExpStorage() * 100 / AExpMachineBlockEntity.XP_CAPACITY;
//		if (percent > 0) {
//			client.getTextureManager().bindTexture(XP);
//			int t = (int) (System.currentTimeMillis() % (16 * 50) / 50);
//			drawTexture(matrices, x + 152, y + 68 - (percent + 1) / 2, 0, t * 50, 16, (percent + 1) / 2, 16, 800);
//		}
//		client.getTextureManager().bindTexture(FRAME);
//		drawTexture(matrices, x + 151, y + 17, 0, 0, 18, 52, 18, 52);
	}

	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		if (expTankButton.isHovered()) {
			renderTooltip(matrices, expTankButton.getTooltip(), x, y);
		} else if (workTimeIsHovered(x, y)) {
			renderTooltip(matrices, new LiteralText(String.format("%.1fs/%.1fs",
					handler.blockEntity.getWorkTime() / 20., handler.blockEntity.getWorkTimeTotal() / 20.)), x, y);
		}
		super.drawMouseoverTooltip(matrices, x, y);
	}

	public abstract boolean workTimeIsHovered(int mx, int my);
}
