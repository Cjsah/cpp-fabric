package net.cpp.ducktyping;

import net.minecraft.item.ItemStack;

public interface IRitualStackHolder {
	void setRitualStack(ItemStack ritualStack);

	ItemStack getRitualStack();
}
