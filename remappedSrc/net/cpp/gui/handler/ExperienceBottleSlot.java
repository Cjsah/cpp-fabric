package net.cpp.gui.handler;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class ExperienceBottleSlot extends Slot {
//	private int amount;
	public ExperienceBottleSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return Items.EXPERIENCE_BOTTLE.equals(stack.getItem());
	}
//	@Override
//	public ItemStack takeStack(int amount) {
//		if (this.hasStack()) {
//			this.amount += Math.min(amount, this.getStack().getCount());
//		}
//		return super.takeStack(amount);
//	}
//	@Override
//	protected void onTake(int amount) {
//		this.amount += amount;
//	}
}
