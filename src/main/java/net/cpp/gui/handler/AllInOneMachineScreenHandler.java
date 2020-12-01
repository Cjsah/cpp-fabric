package net.cpp.gui.handler;

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
	private AllInOneMachineBlockEntity blockEntity;
	private CraftingInventory inputUI;
	private CraftingResultInventory resultUI = new CraftingResultInventory();
	public final PropertyDelegate propertyDelegate;

	public AllInOneMachineScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new AllInOneMachineBlockEntity(), new ArrayPropertyDelegate(1),
				ScreenHandlerContext.EMPTY);
	}

	public AllInOneMachineScreenHandler(int syncId, PlayerInventory playerInventory,
			AllInOneMachineBlockEntity blockEntity, PropertyDelegate propertyDelegate, ScreenHandlerContext context) {
		super(CppScreenHandler.CRAFTING_MACHINE, syncId);
		player = playerInventory.player;
		world = player.world;
		this.blockEntity = blockEntity;
		this.propertyDelegate = propertyDelegate;
		addSlot(new Slot(blockEntity, 0, 8 + 3 * 18, 18));
		addSlot(new Slot(blockEntity, 1, 8 + 4 * 18, 18));
		addSlot(new ExperienceBottleSlot(blockEntity, 2, 8 + 6 * 18, 18));
		addSlot(new ResultSlot(blockEntity, 3, 8 + 3 * 18, 18 + 2 * 18));
		addSlot(new ResultSlot(blockEntity, 4, 8 + 4 * 18, 18 + 2 * 18));
		for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(player.inventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        for (int m = 0; m < 9; ++m) {
            this.addSlot(new Slot(player.inventory, m, 8 + m * 18, 142));
        }
		blockEntity.onOpen(player);
		addProperties(propertyDelegate);
	}

	/*
	 * 以下是AbstractRecipeScreenHandler的方法
	 */
	@Override
	public void populateRecipeFinder(RecipeFinder finder) {
		inputUI.provideRecipeInputs(finder);
	}

	@Override
	public void clearCraftingSlots() {
		inputUI.clear();
		resultUI.clear();
	}

	@Override
	public boolean matches(Recipe<? super Inventory> recipe) {
		return recipe.matches(inputUI, world);
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
		if (id == 1010) {
			blockEntity.shiftOutputDir();
			return true;
		}
		return false;
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index == 0) {
				itemStack2.getItem().onCraft(itemStack2, world, player);
				if (!this.insertItem(itemStack2, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onStackChanged(itemStack2, itemStack);
			} else if (index >= 10 && index < 46) {
				if (!this.insertItem(itemStack2, 1, 10, false)) {
					if (index < 37) {
						if (!this.insertItem(itemStack2, 37, 46, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!this.insertItem(itemStack2, 10, 37, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.insertItem(itemStack2, 10, 46, false)) {
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

			ItemStack itemStack3 = slot.onTakeItem(player, itemStack2);
			if (index == 0) {
				player.dropItem(itemStack3, false);
			}
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
		return slot.inventory != this.resultUI && super.canInsertIntoSlot(stack, slot);
	}

}
