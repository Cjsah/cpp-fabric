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

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Shadow
    private Item item;

    @Shadow
    private CompoundTag tag;

    @Inject(at = @At("HEAD"), method = "updateEmptyState()V")
    private void updateEmptyState(CallbackInfo info) {
        if (this.item != null) {
            if (this.item == CppItems.GREEN_FORCE_OF_WATER) {
                CompoundTag tag = new CompoundTag();
                tag.putString("mode", "water");
                tag.putInt("lava", 0);
                appendTag(tag);
            }else if (this.item == CppItems.CYAN_FORCE_OF_MOUNTAIN) {
                CompoundTag tag = new CompoundTag();
                tag.putBoolean("horizontal", true);
                tag.putInt("level", 2);
                tag.putInt("xp", 0);
                appendTag(tag);
            }
        }
    }

    private void appendTag(CompoundTag tag) {
        CompoundTag newTag;
        if (this.tag == null) {
            newTag = new CompoundTag();
        }else {
            newTag = this.tag;
        }
        for (String name : tag.getKeys()) {
            if (!newTag.contains(name)) {
                newTag.put(name, tag.get(name));
            }
        }
        this.tag = newTag;
    }
}