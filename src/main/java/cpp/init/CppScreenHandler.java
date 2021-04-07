package cpp.init;

import cpp.screen.handler.*;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.SimpleClientHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public final class CppScreenHandler {

	public static final ScreenHandlerType<CraftingMachineScreenHandler> CRAFTING_MACHINE = register(CppBlocks.CRAFTING_MACHINE, CraftingMachineScreenHandler::new);
	public static final ScreenHandlerType<AllInOneMachineScreenHandler> ALL_IN_ONE_MACHINE = register(CppBlocks.ALL_IN_ONE_MACHINE, AllInOneMachineScreenHandler::new);
	public static final ScreenHandlerType<ItemProcessorScreenHandler> ITEM_PROCESSOR = register(CppBlocks.ITEM_PROCESSOR, ItemProcessorScreenHandler::new);
	public static final ScreenHandlerType<MobProjectorScreenHandler> MOB_PROJECTOR = register(CppBlocks.MOB_PROJECTOR, MobProjectorScreenHandler::new);
	public static final ScreenHandlerType<BeaconEnhancerScreenHandler> BEACON_ENHANCER = register(CppBlocks.BEACON_ENHANCER, BeaconEnhancerScreenHandler::new);
	public static final ScreenHandlerType<PortableCraftingTableScreenHandler> PORTABLE_CRAFTING_TABLE = register(CppItems.PORTABLE_CRAFTING_TABLE, PortableCraftingTableScreenHandler::new);
	public static final ScreenHandlerType<PortableCraftingMachineScreenHandler> PORTABLE_CRAFTING_MACHINE = register(CppItems.PORTABLE_CRAFTING_MACHINE,PortableCraftingMachineScreenHandler::new);
	public static final ScreenHandlerType<TradeMachineScreenHandler> TRADE_MACHINE = register(CppBlocks.TRADE_MACHINE,TradeMachineScreenHandler::new);
	public static final ScreenHandlerType<GoldenAnvilScreenHandler> GOLDEN_ANVIL = register(CppBlocks.GOLDEN_ANVIL,GoldenAnvilScreenHandler::new);
	public static final ScreenHandlerType<EmptyBookshelfScreenHandler> EMPTY_BOOKSHELF = register(CppBlocks.EMPTY_BOOKSHELF,EmptyBookshelfScreenHandler::new);
	public static final ScreenHandlerType<ColorPaletteScreenHandler> COLOR_PALETTE = register(CppItems.COLOR_PALETTE,ColorPaletteScreenHandler::new);

	public static void loadClass() {}

	private static <T extends ScreenHandler> ScreenHandlerType<T> register(Block block, SimpleClientHandlerFactory<T> factory) {
		return ScreenHandlerRegistry.registerSimple(Registry.BLOCK.getId(block), factory);
	}

	private static <T extends ScreenHandler> ScreenHandlerType<T> register(Item item, SimpleClientHandlerFactory<T> factory) {
		return ScreenHandlerRegistry.registerSimple(Registry.ITEM.getId(item), factory);
	}

}
