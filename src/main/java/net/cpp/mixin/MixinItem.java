package net.cpp.mixin;

import net.cpp.api.INutrition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import static net.cpp.api.CppFoodOrPotion.map;

@Mixin(Item.class)
public class MixinItem implements INutrition {

    @Override
    public int getNutrition(ItemStack itemStack) {
        if (map.get(itemStack.getItem()) != null) {
            return map.get(itemStack.getItem());
        }
        return 0;
    }
}
