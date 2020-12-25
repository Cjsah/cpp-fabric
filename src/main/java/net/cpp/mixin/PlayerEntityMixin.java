package net.cpp.mixin;

import net.cpp.init.CppItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Shadow
    private ItemStack selectedItem;

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void tick() {
        if (this.selectedItem.getItem() == CppItems.MAGNET) {
            // 不会写....
        }
    }
}
