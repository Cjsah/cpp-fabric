package cpp.mixin;

import cpp.ducktyping.INutrition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import static cpp.item.CppFoodOrPotion.map;

@Mixin(Item.class)
public class MixinItem implements INutrition {

    @Override
    public int getNutrition(ItemStack itemStack) {
    	return map.getOrDefault(itemStack.getItem(), 0);
    }
}
