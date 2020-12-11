package net.cpp.gui.handler;

import static net.cpp.gui.handler.SlotTool.x;
import static net.cpp.gui.handler.SlotTool.y;

import net.cpp.block.entity.AllInOneMachineBlockEntity;
import net.cpp.block.entity.MobProjectorBlockEntity;
import net.cpp.init.CppScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class MobProjectorScreenHandler extends AMachineScreenHandler {
	private PlayerEntity player;
//	private World world;
	public final MobProjectorBlockEntity blockEntity;

	public MobProjectorScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new MobProjectorBlockEntity());
	}

	public MobProjectorScreenHandler(int syncId, PlayerInventory playerInventory,
			MobProjectorBlockEntity blockEntity) {
		super(CppScreenHandler.MOB_PROJECTOR, syncId, playerInventory, blockEntity);
		player = playerInventory.player;
//		world = player.world;
		this.blockEntity = blockEntity;
		addSlot(new Slot(blockEntity, 0, x(4), y(0)));
		addSlot(new Slot(blockEntity, 1, x(4), y(1)));
		addSlot(new Slot(blockEntity, 2, x(4), y(2)));
		addSlot(new ExperienceBottleSlot(blockEntity, 3, x(6), y(0)));
		blockEntity.onOpen(player);
		addProperties(blockEntity.propertyDelegate);
	}

	/*
	 * 以下是ScreenHandler的方法
	 */

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
//		Slot slot = (Slot) this.slots.get(index);
//		if (slot != null && slot.hasStack()) {
//			ItemStack itemStack2 = slot.getStack();
//			itemStack = itemStack2.copy();
//			if (index >= 36 && index < 42) {
//				if (!this.insertItem(itemStack2, 0, 36, true))
//					return ItemStack.EMPTY;
//				slot.onStackChanged(itemStack2, itemStack);
//			} else {
//				if (getSlot(38).canInsert(itemStack2)) {
//					if (!this.insertItem(itemStack2, 38, 39, false))
//						return ItemStack.EMPTY;
//				} else if (!this.insertItem(itemStack2, 36, 38, false)) {
//					return ItemStack.EMPTY;
//				} else if (super.transferSlot(player, index) == ItemStack.EMPTY) {
//					return ItemStack.EMPTY;
//				}
//			}
//			if (itemStack2.isEmpty()) {
//				slot.setStack(ItemStack.EMPTY);
//			} else {
//				slot.markDirty();
//			}
//			if (itemStack2.getCount() == itemStack.getCount()) {
//				return ItemStack.EMPTY;
//			}
//			slot.onTakeItem(player, itemStack2);
//		}
		return itemStack;
	}

	@Override
	public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
		return slot.inventory != this.blockEntity && super.canInsertIntoSlot(stack, slot);
	}
}
