package cpp.screen.handler;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
/**
 * 成品槽，不允许放入物品
 * @author Ph-苯
 *
 */
public class ResultSlot extends Slot {

	public ResultSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	@Override
	public boolean canInsert(ItemStack stack) {
		return false;
	}
}
