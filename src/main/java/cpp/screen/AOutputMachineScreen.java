package cpp.screen;

import cpp.screen.handler.AOutputMachineScreenHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public abstract class AOutputMachineScreen<T extends AOutputMachineScreenHandler> extends AMachineScreen<T> {
	public final OutputDirectionButton oButton = new OutputDirectionButton(buttonWidget -> client.interactionManager.clickButton(this.handler.syncId, OutputDirectionButton.SYNC_ID), handler.blockEntity);
	public static final TranslatableText CLICK_TO_SHIFT = new TranslatableText("gui.click_to_shift");
	public AOutputMachineScreen(T handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init() {
		super.init();
		addButton(oButton);
	}

	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		if (oButton.isHovered())
			renderTooltip(matrices, oButton.getTooltip(), x, y);
		super.drawMouseoverTooltip(matrices, x, y);
	}
}
