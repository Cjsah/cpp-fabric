package net.cpp.init;

import net.cpp.gui.handler.*;
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
	public static final ScreenHandlerType<ItemProcessorScreenHandler> ITEM_PROCESSOR = register(CppBlocks.ITEM_PROCESSER, ItemProcessorScreenHandler::new);
	public static final ScreenHandlerType<MobProjectorScreenHandler> MOB_PROJECTOR = register(CppBlocks.MOB_PROJECTOR, MobProjectorScreenHandler::new);
	public static final ScreenHandlerType<PortableCraftingTableScreenHandler> PORTABLE_CRAFTING_TABLE_SCREEN_HANDLER = register(CppItems.PORTABLE_CRAFTING_TABLE, PortableCraftingTableScreenHandler::new);

	public static void register() {
	}

	private static <T extends ScreenHandler> ScreenHandlerType<T> register(Block block, SimpleClientHandlerFactory<T> factory) {
		return ScreenHandlerRegistry.registerSimple(Registry.BLOCK.getId(block), factory);
	}

	
	private static <T extends ScreenHandler> ScreenHandlerType<T> register(Item item, SimpleClientHandlerFactory<T> factory) {
		return ScreenHandlerRegistry.registerSimple(Registry.ITEM.getId(item), factory);
	}

}
