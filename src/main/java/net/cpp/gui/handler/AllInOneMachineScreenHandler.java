package net.cpp.gui.handler;

import static net.cpp.gui.handler.SlotTool.x;
import static net.cpp.gui.handler.SlotTool.y;

import net.cpp.block.entity.AllInOneMachineBlockEntity;
import net.cpp.init.CppScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

/**
 * 合成器GUI
 *
 * @author Ph-苯
 *
 */
public class AllInOneMachineScreenHandler extends AMachineScreenHandler {
//	public AllInOneMachineScreen screen;
	private PlayerEntity player;
//	private World world;
	public final AllInOneMachineBlockEntity blockEntity;

	public AllInOneMachineScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new AllInOneMachineBlockEntity());
	}

	public AllInOneMachineScreenHandler(int syncId, PlayerInventory playerInventory,
			AllInOneMachineBlockEntity blockEntity) {
		super(CppScreenHandler.ALL_IN_ONE_MACHINE, syncId, playerInventory, blockEntity);
		player = playerInventory.player;
//		world = player.world;
		this.blockEntity = blockEntity;
		addSlot(new Slot(blockEntity, 0, x(3), y(0)));
		addSlot(new Slot(blockEntity, 1, x(4), y(0)));
		addSlot(new ExperienceBottleSlot(blockEntity, 2, x(6), y(0)));
		addSlot(new ResultSlot(blockEntity, 3, x(3), y(2)));
		addSlot(new ResultSlot(blockEntity, 4, x(4), y(2)));
		blockEntity.onOpen(player);
		addProperties(blockEntity.propertyDelegate);
	}

//	/*
//	 * 以下是AbstractRecipeScreenHandler的方法
//	 */
//	@Override
//	public void populateRecipeFinder(RecipeFinder finder) {
////		blockEntity.provideRecipeInputs(finder);
//	}
//
//	@Override
//	public void clearCraftingSlots() {
//		blockEntity.clear();
//	}
//
//	@Override
//	public boolean matches(Recipe<? super Inventory> recipe) {
//		return recipe.matches(blockEntity, world);
//	}
//
//	@Override
//	public int getCraftingResultSlotIndex() {
//		return 0;
//	}
//
//	@Override
//	public int getCraftingWidth() {
//		return 2;
//	}
//
//	@Override
//	public int getCraftingHeight() {
//		return 1;
//	}
//
//	@Override
//	@Environment(EnvType.CLIENT)
//	public int getCraftingSlotCount() {
//		return 3;
//	}
//
//	@Override
//	@Environment(EnvType.CLIENT)
//	public RecipeBookCategory getCategory() {
//		throw new UnsupportedOperationException("多功能一体机配方没有配方书页面");
//	}

	/*
	 * 以下是ScreenHandler的方法
	 */
	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		boolean clicked = false;
		switch (id) {
		case 1011:
			blockEntity.shiftTemperature();
			clicked = true;
			break;
		case 1012:
			blockEntity.shiftPressure();
			clicked = true;
			break;
		}
		return super.onButtonClick(player, id) || clicked;
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index >= 36 && index < 42) {
				if (!this.insertItem(itemStack2, 0, 36, true))
					return ItemStack.EMPTY;
				slot.onStackChanged(itemStack2, itemStack);
			} else {
				if (getSlot(38).canInsert(itemStack2)) {
					if (!this.insertItem(itemStack2, 38, 39, false))
						return ItemStack.EMPTY;
				} else if (!this.insertItem(itemStack2, 36, 38, false)) {
					return ItemStack.EMPTY;
					// TODO 尽量让两个原料格物品不同
//					ItemStack input1 = getSlot(0).getStack(), input2 = getSlot(1).getStack();
//					if (input1.isEmpty() && !input2.getItem().equals(itemStack2.getItem())) {
//						if (!insertItem(itemStack2, 0, 1, false))
//							return ItemStack.EMPTY;
//					} else if (input2.isEmpty() && !input1.getItem().equals(itemStack2.getItem())) {
//						if (insertItem(itemStack2, 1, 2, false))
//							return ItemStack.EMPTY;
//					} else if (input1.isEmpty() && input2.isEmpty()) {
//						if (insertItem(itemStack2, 0, 2, false))
//						return ItemStack.EMPTY;
//					}
				} else if (super.transferSlot(player, index) == ItemStack.EMPTY) {
					return ItemStack.EMPTY;
				}
			}
			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTakeItem(player, itemStack2);
		}
		return itemStack;
	}

	@Override
	public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
		return slot.inventory != this.blockEntity && super.canInsertIntoSlot(stack, slot);
	}
	/*
	 * 以下是自定义方法
	 */

//	public class InputSlot extends Slot {
//		public final int index;
//
//		public InputSlot(Inventory inventory, int index, int x, int y) {
//			super(inventory, index, x, y);
//			this.index = index;
//		}
//
//		@Override
//		public boolean canInsert(ItemStack stack) {
//			return super.canInsert(stack);
//		}
//	}
}
