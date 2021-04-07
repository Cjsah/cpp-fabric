package cpp.screen;

import cpp.api.Utils;
import cpp.init.CppBlocks;
import cpp.screen.handler.ItemProcessorScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemProcessorScreen extends AOutputMachineScreen<ItemProcessorScreenHandler> {
	public static final Identifier BACKGROUND = getBackgroundByName(Registry.BLOCK.getId(CppBlocks.ITEM_PROCESSOR).getPath());

	public ItemProcessorScreen(ItemProcessorScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}
	@Override
	protected Identifier getBackground() {
		return BACKGROUND;
	}

	@Override
	protected void init() {
		super.init();
		oButton.setPos(x + Utils.x(4), y + Utils.y(2));
	}
}
