package cpp.screen.handler;

import cpp.block.entity.CraftingMachineBlockEntity;
import cpp.init.CppScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * 合成器GUI
 *
 * @author Ph-苯
 *
 */
public class CraftingMachineScreenHandler extends AOutputMachineScreenHandler {
	private PlayerEntity player;
	private CraftingMachineBlockEntity blockEntity;
	private CraftingInventory inputUI;
	private CraftingResultInventory resultUI = new CraftingResultInventory();
	public final PropertyDelegate propertyDelegate;

	public CraftingMachineScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new CraftingMachineBlockEntity(playerInventory.player.getBlockPos(), playerInventory.player.world.getBlockState(playerInventory.player.getBlockPos())), new ArrayPropertyDelegate(1), ScreenHandlerContext.EMPTY);
	}

	public CraftingMachineScreenHandler(int syncId, PlayerInventory playerInventory, CraftingMachineBlockEntity blockEntity, PropertyDelegate propertyDelegate, ScreenHandlerContext context) {
		super(CppScreenHandler.CRAFTING_MACHINE, syncId, playerInventory, blockEntity);
		player = playerInventory.player;
		this.blockEntity = blockEntity;
		inputUI = blockEntity.getInputInventory();
		this.propertyDelegate = propertyDelegate;

		for (int m = 0; m < 3; ++m) {
			for (int l = 0; l < 3; ++l) {
				this.addSlot(new Slot(inputUI, l + m * 3, 30 + l * 18, 17 + m * 18));
			}
		}
		addSlot(new CppCraftingResultSlot(player, inputUI, resultUI, 0, 124, 35));

		blockEntity.onOpen(player);
		updateResultUI();
	}

//	/*
//	 * 以下是AbstractRecipeScreenHandler的方法
//	 */
//	@Override
//	public void populateRecipeFinder(RecipeFinder finder) {
//		inputUI.provideRecipeInputs(finder);
//	}
//
//	@Override
//	public void clearCraftingSlots() {
//		inputUI.clear();
//		resultUI.clear();
//	}
//
//	@Override
//	public boolean matches(Recipe<? super Inventory> recipe) {
//		return recipe.matches(inputUI, world);
//	}
//
//	@Override
//	public int getCraftingResultSlotIndex() {
//		return 0;
//	}
//
//	@Override
//	public int getCraftingWidth() {
//		return blockEntity.getInputInventory().getWidth();
//	}
//
//	@Override
//	public int getCraftingHeight() {
//		return blockEntity.getInputInventory().getHeight();
//	}
//
//	@Override
//	@Environment(EnvType.CLIENT)
//	public int getCraftingSlotCount() {
//		return 10;
//	}
//
//	@Override
//	@Environment(EnvType.CLIENT)
//	public RecipeBookCategory getCategory() {
//		throw new UnsupportedOperationException("合成器配方没有配方书页面");
//	}

	/*
	 * 以下是ScreenHandler的方法
	 */

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index >= 36 && index < 36+blockEntity.size()) {
				if (index == 45)
//					this.context.run((world, blockPos) -> {
					itemStack2.getItem().onCraft(itemStack2, blockEntity.getWorld(), player);
//					});
				if (!this.insertItem(itemStack2, 0, 36, true)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickTransfer(itemStack2, itemStack);
			} else if (index >= 0 && index < 36) {
				if (!this.insertItem(itemStack2, 36, 45, false) && super.transferSlot(player, index) == ItemStack.EMPTY) {
					return ItemStack.EMPTY;
				}
			}
			slot.onTakeItem(player, itemStack2);
			if (index == 45) {
				player.dropItem(itemStack2, false);
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void close(PlayerEntity player) {
		super.close(player);
		blockEntity.onClose(player);
	}

	@Override
	public void onContentChanged(Inventory inventory) {
//		updateResultUI();
//		this.context.run((world2, blockPos) -> {
		updateResultUI();
//		});
	}

	@Override
	public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
		return slot.inventory != this.resultUI && super.canInsertIntoSlot(stack, slot);
	}

	/*
	 * 以下是自定义方法
	 */
	private void updateResultUI() {
		if (player instanceof ServerPlayerEntity) {
			resultUI.setStack(0, blockEntity.getResult());
//			System.out.println(resultUI.getStack(0));
//			System.out.println(blockEntity.getInvStackList());
			((ServerPlayerEntity) player).networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 45, resultUI.getStack(0)));
		}
	}
}
