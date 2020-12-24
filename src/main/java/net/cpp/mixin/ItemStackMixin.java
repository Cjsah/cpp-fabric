package net.cpp.mixin;

import net.cpp.init.CppItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    private Item item;

    @Shadow
    private CompoundTag tag;

    @Inject(at = @At("HEAD"), method = "updateEmptyState()V")
    private void updateEmptyState(CallbackInfo info) {
        if (this.item != null) {
            if (this.item == CppItems.GREEN_FORCE_OF_WATER) {
                CompoundTag tag;
                if (this.tag == null) {
                    tag = new CompoundTag();
                }else {
                    tag = this.tag;
                }
                if (!tag.contains("mode"))
                    tag.putString("mode", "water");
                if (!tag.contains("lava"))
                    tag.putInt("lava", 0);
                this.tag = tag;
            }
        }
    }
}