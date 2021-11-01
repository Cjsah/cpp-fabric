package cpp.mixin;

import cpp.init.CppItems;
import cpp.item.CharacterItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AnvilScreenHandler.class)
public class MixinAnvilScreenHandler {

    @Shadow
    private String newItemName;

    @Inject(at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/item/ItemStack;setCustomName(Lnet/minecraft/text/Text;)Lnet/minecraft/item/ItemStack;"
    ), method = "updateResult", locals = LocalCapture.CAPTURE_FAILHARD)
    public void result(CallbackInfo ci, ItemStack itemStack, int i, int j, int k, ItemStack itemStack2) {
        if (itemStack2.isOf(CppItems.CHARACTER)) {
            try {
                int target = Integer.parseInt(this.newItemName);
                if (target >=1 && target <= CharacterItem.MAX) {
                    itemStack2.removeCustomName();
                    itemStack2.getOrCreateTag().putInt("character", target);
                }
            }catch (NumberFormatException ignore) {
            }
        }
    }
}
