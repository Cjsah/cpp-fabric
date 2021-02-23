package net.cpp.mixin;

import net.cpp.misc.ICppGameHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInCppGameHud implements ICppGameHud {


//    @Inject(method="renderStatusEffectOverlay", at=@At("RETURN"))
//    private void afterRenderStatusEffects(MatrixStack stack, CallbackInfo info) {
//        gui.afterRenderStatusEffects(stack, 0);
//    }

    @Inject(method="render", at=@At(
            value="FIELD",
            target="Lnet/minecraft/client/option/GameOptions;debugEnabled:Z",
            opcode = Opcodes.GETFIELD,
            args = {"log=false"}))
    private void beforeRenderDebugScreen(MatrixStack matrix, float tickDelta, CallbackInfo info) {
        GUI.render(matrix);
    }

}
