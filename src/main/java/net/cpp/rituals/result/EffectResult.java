package net.cpp.rituals.result;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EffectResult extends ARitualResult {
    private final Item rare;
    private final Item agentia;
    public EffectResult(Item rare, Item agentia) {
        this.rare = rare;
        this.agentia = agentia;
    }

    @Override
    public ItemStack get() {
        return null;
    }
}
