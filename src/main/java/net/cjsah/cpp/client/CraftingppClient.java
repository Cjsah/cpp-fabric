package net.cjsah.cpp.client;

import net.cjsah.cpp.client.gui.screen.CraftingMachineScreen;
import net.cjsah.cpp.init.CraftingppScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class CraftingppClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(CraftingppScreenHandlers.CRAFTING_SCREEN_SCREEN_HANDLER, CraftingMachineScreen::new);
    }
}
