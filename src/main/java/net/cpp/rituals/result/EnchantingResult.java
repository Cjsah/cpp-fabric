package net.cpp.rituals.result;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EnchantingResult extends ARitualResult {
    private final Item rare1;
    private final Item rare2;
    public EnchantingResult(Item rare1, Item rare2) {
        this.rare1 = rare1;
        this.rare2 = rare2;
    }

    @Override
    public ItemStack get() {
        return null;
    }
}
