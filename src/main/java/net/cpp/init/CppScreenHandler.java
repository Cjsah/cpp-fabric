package net.cpp.init;

import net.cpp.gui.handler.CraftingMachineScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public final class CppScreenHandler {

    public static final ScreenHandlerType<CraftingMachineScreenHandler> CRAFTING_MACHINE = ScreenHandlerRegistry
            .registerSimple(Registry.BLOCK.getId(CppBlocks.CRAFTING_MACHINE), CraftingMachineScreenHandler::new);


    public static void register() {

    }


}
