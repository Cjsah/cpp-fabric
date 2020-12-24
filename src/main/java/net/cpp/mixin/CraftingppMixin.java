package net.cpp.mixin;

import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class CraftingppMixin {
    @Inject(at = @At("HEAD"), method = "run()V")
    private void run(CallbackInfo info) {
        LogManager.getLogger("Craftingpp").info("welcome to use cpp");
    }
}
