package net.cpp;

import net.cpp.screen.CraftingMachineScreen;
import net.cpp.screen.CraftingMachineScreenHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.impl.client.container.ScreenProviderRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class Client implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ScreenRegistry.register(Registeror.CRAFTING_MACHINE_HANDLER_TYPE, CraftingMachineScreen::new);
	}

}
