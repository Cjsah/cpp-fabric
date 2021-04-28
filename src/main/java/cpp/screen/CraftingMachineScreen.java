package cpp.screen;

import cpp.init.CppBlocks;
import cpp.screen.handler.CraftingMachineScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CraftingMachineScreen extends AOutputMachineScreen<CraftingMachineScreenHandler> {
	public static final Identifier BACKGROUND = getBackgroundByName(Registry.BLOCK.getId(CppBlocks.CRAFTING_MACHINE).getPath());

	public CraftingMachineScreen(CraftingMachineScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected Identifier getBackground() {
		return BACKGROUND;
	}

	protected void init() {
		super.init();
		oButton.setPos(field_2776 + 94, field_2800 + 57);
	}
}
