package net.cpp.mixin;

import net.cpp.ducktyping.INutrition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import static net.cpp.api.CppFoodOrPotion.map;

@Mixin(Item.class)
@SuppressWarnings("unused")
public class MixinItem implements INutrition {

    @Override
    public int getNutrition(ItemStack itemStack) {
    	return map.getOrDefault(itemStack.getItem(), 0);
    }
}
