package net.cpp.rituals.result;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ARitualResult {
    ItemStack get(@Nullable Item... items);
}
