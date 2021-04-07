package cpp.block.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

/**
 * 合成器的原料栏
 * 
 * @author Ph-苯
 *
 */
public class CppCraftingInventory extends CraftingInventory {
	private ScreenHandler handler = new ScreenHandler(null, -1) {
		@Override
		public boolean canUse(PlayerEntity playerIn) {
			return true;
		}
	};

	public CppCraftingInventory() {
		super(new ScreenHandler(null, -1) {
			@Override
			public boolean canUse(PlayerEntity playerIn) {
				return true;
			}
		}, 3, 3);
	}

	public ScreenHandler getHandler() {
		return handler;
	}

	public void setHandler(ScreenHandler handler) {
		this.handler = handler;
	}

	public ItemStack removeStack(int slot) {
		ItemStack rst = super.removeStack(slot);
		getHandler().onContentChanged(this);
		return rst;
	}

	public ItemStack removeStack(int slot, int amount) {
		ItemStack rst = super.removeStack(slot, amount);
		getHandler().onContentChanged(this);
		return rst;
	}

	public void setStack(int slot, ItemStack stack) {
		super.setStack(slot, stack);
		getHandler().onContentChanged(this);
	}

}
