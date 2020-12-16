package net.cpp;

import net.cpp.gui.screen.AllInOneMachineScreen;
import net.cpp.gui.screen.BeaconEnhancerScreen;
import net.cpp.gui.screen.CraftingMachineScreen;
import net.cpp.gui.screen.ItemProcessorScreen;
import net.cpp.gui.screen.MobProjectorScreen;
import net.cpp.gui.screen.PortableCraftingMachineScreen;
import net.cpp.init.CppScreenHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;

@Environment(EnvType.CLIENT)
public class CraftingppClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ScreenRegistry.register(CppScreenHandler.CRAFTING_MACHINE, CraftingMachineScreen::new);
		ScreenRegistry.register(CppScreenHandler.ALL_IN_ONE_MACHINE, AllInOneMachineScreen::new);
		ScreenRegistry.register(CppScreenHandler.ITEM_PROCESSOR, ItemProcessorScreen::new);
		ScreenRegistry.register(CppScreenHandler.MOB_PROJECTOR, MobProjectorScreen::new);
		ScreenRegistry.register(CppScreenHandler.BEACON_ENHANCER, BeaconEnhancerScreen::new);
		ScreenRegistry.register(CppScreenHandler.PORTABLE_CRAFTING_TABLE_SCREEN_HANDLER, CraftingScreen::new);
		ScreenRegistry.register(CppScreenHandler.PORTABLE_CRAFTING_MACHINE_SCREEN_HANDLER, PortableCraftingMachineScreen::new);
	}

}
