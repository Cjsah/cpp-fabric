package cpp.screen.handler;

import cpp.block.entity.AMachineBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class AMachineScreenHandler extends ScreenHandler {
	protected PlayerInventory playerInventory;
	public final AMachineBlockEntity blockEntity;

	public AMachineScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, AMachineBlockEntity blockEntity) {
		super(type, syncId);
		this.playerInventory = playerInventory;
		this.blockEntity = blockEntity;
		for (int m = 0; m < 3; ++m) {
			for (int l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
			}
		}
		for (int m = 0; m < 9; ++m) {
			this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
		}
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		return super.onButtonClick(player, id);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return blockEntity.canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack = slot.getStack();
			if (index >= 27 && index < 36) {
				insertItem(itemStack, 0, 27, false);
			} else if (index >= 0 && index < 27) {
				insertItem(itemStack, 27, 36, false);
			}
			slot.onTakeItem(player, itemStack);
		}
		return ItemStack.EMPTY;
	}

	protected boolean insertItem(ItemStack itemStack, int index) {
		return insertItem(itemStack, index, index + 1, false);
	}
}
