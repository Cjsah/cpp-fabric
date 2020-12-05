package net.cpp.gui.handler;

import static net.cpp.gui.handler.SlotTool.*;
import net.cpp.block.entity.AllInOneMachineBlockEntity;
import net.cpp.gui.screen.AllInOneMachineScreen;
import net.cpp.init.CppScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

/**
 * 合成器GUI
 *
 * @author Ph-苯
 *
 */
public class AllInOneMachineScreenHandler extends AbstractRecipeScreenHandler<Inventory> {
	public AllInOneMachineScreen screen;
	private PlayerEntity player;
	private World world;
	public final AllInOneMachineBlockEntity blockEntity;

	public AllInOneMachineScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new AllInOneMachineBlockEntity());
	}

	public AllInOneMachineScreenHandler(int syncId, PlayerInventory playerInventory,
			AllInOneMachineBlockEntity blockEntity) {
		super(CppScreenHandler.ALL_IN_ONE_MACHINE, syncId);
		player = playerInventory.player;
		world = player.world;
		this.blockEntity = blockEntity;
		addSlot(new Slot(blockEntity, 0, x(3), y(0)));
		addSlot(new Slot(blockEntity, 1, x(4), y(0)));
		addSlot(new ExperienceBottleSlot(blockEntity, 2, x(6), y(0)));
		addSlot(new ResultSlot(blockEntity, 3, x(3), y(2)));
		addSlot(new ResultSlot(blockEntity, 4, x(4), y(2)));
		for (int m = 0; m < 3; ++m) {
			for (int l = 0; l < 9; ++l) {
				this.addSlot(new Slot(player.inventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
			}
		}
		for (int m = 0; m < 9; ++m) {
			this.addSlot(new Slot(player.inventory, m, 8 + m * 18, 142));
		}
		blockEntity.onOpen(player);
		addProperties(blockEntity.propertyDelegate);
	}

	/*
	 * 以下是AbstractRecipeScreenHandler的方法
	 */
	@Override
	public void populateRecipeFinder(RecipeFinder finder) {
//		blockEntity.provideRecipeInputs(finder);
	}

	@Override
	public void clearCraftingSlots() {
		blockEntity.clear();
	}

	@Override
	public boolean matches(Recipe<? super Inventory> recipe) {
		return recipe.matches(blockEntity, world);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return 0;
	}

	@Override
	public int getCraftingWidth() {
		return 2;
	}

	@Override
	public int getCraftingHeight() {
		return 1;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public int getCraftingSlotCount() {
		return 3;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public RecipeBookCategory getCategory() {
		throw new UnsupportedOperationException("多功能一体机配方没有配方书页面");
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return blockEntity.canPlayerUse(player);
	}

	/*
	 * 以下是ScreenHandler的方法
	 */
	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		boolean clicked = false;
		switch (id) {
		case 1010:
			blockEntity.shiftOutputDir();
			clicked = true;
			break;
		case 1011:
			blockEntity.shiftTemperature();
			clicked = true;
			break;
		case 1012:
			blockEntity.shiftPressure();
			clicked = true;
			break;
		default:
			break;
		}
		return clicked;
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index >= 0 && index <= 4) {
				if (!this.insertItem(itemStack2, 5, 41, true)) {
					return ItemStack.EMPTY;
				}
				slot.onStackChanged(itemStack2, itemStack);
			} else {
				if (getSlot(2).canInsert(itemStack2)) {
					if (!this.insertItem(itemStack2, 2, 3, false))
						return ItemStack.EMPTY;
				} else {
					if (!this.insertItem(itemStack2, 0, 2, false))
						return ItemStack.EMPTY;
					//TODO 尽量让两个原料格物品不同
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
				}
			}
			if (!this.insertItem(itemStack2, 5, 41, false)) {
				return ItemStack.EMPTY;
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
	public void close(PlayerEntity player) {
		super.close(player);
		blockEntity.onClose(player);
	}

	@Override
	public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
		return slot.inventory != this.blockEntity && super.canInsertIntoSlot(stack, slot);
	}
	/*
	 * 以下是自定义方法
	 */

	public class InputSlot extends Slot {
		public final int index;

		public InputSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
			this.index = index;
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return super.canInsert(stack);
		}
	}
}
