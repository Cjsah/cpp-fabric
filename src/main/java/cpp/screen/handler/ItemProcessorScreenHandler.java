package cpp.screen.handler;

import cpp.api.Utils;
import cpp.block.entity.ItemProcessorBlockEntity;
import cpp.init.CppScreenHandler;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ItemProcessorScreenHandler extends AOutputMachineScreenHandler {
	public final ItemProcessorBlockEntity blockEntity;

	public ItemProcessorScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new ItemProcessorBlockEntity(playerInventory.player.getBlockPos(), playerInventory.player.getBlockState()));
	}

	public ItemProcessorScreenHandler(int syncId, PlayerInventory playerInventory, ItemProcessorBlockEntity blockEntity) {
		super(CppScreenHandler.ITEM_PROCESSOR, syncId, playerInventory, blockEntity);
		this.blockEntity = blockEntity;

		addSlot(new Slot(blockEntity, 0, Utils.x(3), Utils.y(0)));
		addSlot(new Slot(blockEntity, 1, Utils.x(3), Utils.y(1)));
		addSlot(new ResultSlot(blockEntity, 2, Utils.x(6), Utils.y(1)));
		addSlot(new ResultSlot(blockEntity, 3, Utils.x(7), Utils.y(1)));
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return ((LootableContainerBlockEntity) blockEntity).canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			if (index >= 36 && index < 36+ blockEntity.size()) {
				this.insertItem(itemStack2, 0, 36, true);
			} else {
				if (!(ItemProcessorBlockEntity.RECIPES.containsKey(itemStack2.getItem()) && insertItem(itemStack2, 36, 37, false) || insertItem(itemStack2, 37, 38, false))) {
					super.transferSlot(player, index);
				}
			}
		}
		return ItemStack.EMPTY;
	}
}
