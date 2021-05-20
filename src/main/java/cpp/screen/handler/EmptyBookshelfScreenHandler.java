package cpp.screen.handler;

import cpp.api.Utils;
import cpp.block.entity.EmptyBookshelfBlockEntity;
import cpp.init.CppScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class EmptyBookshelfScreenHandler extends AMachineScreenHandler {
	public final EmptyBookshelfBlockEntity blockEntity;

	public EmptyBookshelfScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new EmptyBookshelfBlockEntity(playerInventory.player.getBlockPos(), playerInventory.player.world.getBlockState(playerInventory.player.getBlockPos())));
	}

	public EmptyBookshelfScreenHandler(int syncId, PlayerInventory playerInventory, EmptyBookshelfBlockEntity blockEntity) {
		super(CppScreenHandler.EMPTY_BOOKSHELF, syncId, playerInventory, blockEntity);
		this.blockEntity = blockEntity;
		for (int i = 0; i < 3; i++)
			addSlot(new BookshelfSlot(blockEntity, i, Utils.x(3 + i), Utils.y(1)));
		blockEntity.onOpen(playerInventory.player);
	}

	public ItemStack transferSlot(PlayerEntity player, int index) {
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack = slot.getStack();
			if (index >= 36) {
				insertItem(itemStack, 0, 36, true);
			} else if (index < 36) {
				insertItem(itemStack, 36, 36 + blockEntity.size(), false);
			}
		}
		return ItemStack.EMPTY;
	}
/**
 * 空书架槽，只允许放入{@link EmptyBookshelfBlockEntity#STORABLE}中的物品
 * @author Ph-苯
 *
 */
	public static class BookshelfSlot extends Slot {
		public BookshelfSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return EmptyBookshelfBlockEntity.STORABLE.contains(stack.getItem());
		}
	}
	@Override
	public void close(PlayerEntity player) {
		blockEntity.onClose(player);
		
		super.close(player);
	}
}
