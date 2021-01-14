package net.cpp.api;

import net.minecraft.item.ItemStack;

public interface IRitualStackHolder {
	void setRitualStack(ItemStack ritualStack);

	ItemStack getRitualStack();
}
