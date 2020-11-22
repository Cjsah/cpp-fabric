package net.cjsah.cpp.client;

import net.cjsah.cpp.gui.screen.CraftingMachineScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.container.ScreenProviderRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CraftingppClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenProviderRegistryImpl.INSTANCE.registerFactory(new Identifier("cpp","crafting_machine"), container -> {
            assert MinecraftClient.getInstance().player != null;
            return new CraftingMachineScreen((CraftingScreenHandler) container, MinecraftClient.getInstance().player.inventory, new TranslatableText("cpp.container"));
        });
    }
}
