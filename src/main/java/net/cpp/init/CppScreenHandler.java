package net.cpp.init;

import net.cpp.gui.handler.AllInOneMachineScreenHandler;
import net.cpp.gui.handler.CraftingMachineScreenHandler;
import net.cpp.gui.handler.ItemProcessorScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.SimpleClientHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public final class CppScreenHandler {

	public static final ScreenHandlerType<CraftingMachineScreenHandler> CRAFTING_MACHINE = register(CppBlocks.CRAFTING_MACHINE, CraftingMachineScreenHandler::new);
	public static final ScreenHandlerType<AllInOneMachineScreenHandler> ALL_IN_ONE_MACHINE = register(CppBlocks.ALL_IN_ONE_MACHINE, AllInOneMachineScreenHandler::new);
	public static final ScreenHandlerType<ItemProcessorScreenHandler> ITEM_PROCESSOR = register(CppBlocks.ITEM_PROCESSER, ItemProcessorScreenHandler::new);

	public static void register() {

	}

	private static <T extends ScreenHandler> ScreenHandlerType<T> register(Block block, SimpleClientHandlerFactory<T> factory) {
		return ScreenHandlerRegistry.registerSimple(Registry.BLOCK.getId(block), factory);
	}

}
