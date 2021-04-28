package cpp.screen;

import static cpp.api.Utils.x;
import static cpp.api.Utils.y;

import cpp.init.CppBlocks;
import cpp.screen.handler.GoldenAnvilScreenHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GoldenAnvilScreen extends AExpMachineScreen<GoldenAnvilScreenHandler> {
	public static final Identifier BACKGROUND = getBackgroundByName(Registry.BLOCK.getId(CppBlocks.GOLDEN_ANVIL).getPath());

	public GoldenAnvilScreen(GoldenAnvilScreenHandler handler, PlayerInventory inventory, Text title) {
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
		oButton.setPos(field_2776 + x(5), field_2800 + y(2));
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		super.drawBackground(matrices, delta, mouseX, mouseY);
	}
}
