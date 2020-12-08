package net.cpp;

import net.cpp.gui.screen.AllInOneMachineScreen;
import net.cpp.gui.screen.CraftingMachineScreen;
import net.cpp.gui.screen.ItemProcessorScreen;
import net.cpp.init.CppScreenHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class CraftingppClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(CppScreenHandler.CRAFTING_MACHINE, CraftingMachineScreen::new);
        ScreenRegistry.register(CppScreenHandler.ALL_IN_ONE_MACHINE, AllInOneMachineScreen::new);
        ScreenRegistry.register(CppScreenHandler.ITEM_PROCESSOR, ItemProcessorScreen::new);
    }

}
