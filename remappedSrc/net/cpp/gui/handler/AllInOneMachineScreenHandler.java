package net.cpp.gui.handler;

import net.cpp.api.CodingTool;
import net.cpp.block.entity.AllInOneMachineBlockEntity;
import net.cpp.gui.screen.AllInOneMachineScreen;
import net.cpp.init.CppScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

/**
 * 合成器GUI
 *
 * @author Ph-苯
 *
 */
public class AllInOneMachineScreenHandler extends AExpMachineScreenHandler {
//	public AllInOneMachineScreen screen;
	private PlayerEntity player;
//	private World world;
	public final AllInOneMachineBlockEntity blockEntity;

	public AllInOneMachineScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new AllInOneMachineBlockEntity(playerInventory.player.getBlockPos(), playerInventory.player.getBlockState()));
	}

	public AllInOneMachineScreenHandler(int syncId, PlayerInventory playerInventory, AllInOneMachineBlockEntity blockEntity) {
		super(CppScreenHandler.ALL_IN_ONE_MACHINE, syncId, playerInventory, blockEntity);
		player = playerInventory.player;
		this.blockEntity = blockEntity;
		addSlot(new Slot(blockEntity, 1, CodingTool.x(3), CodingTool.y(0)));
		addSlot(new Slot(blockEntity, 2, CodingTool.x(4), CodingTool.y(0)));
		addSlot(new ResultSlot(blockEntity, 3, CodingTool.x(3), CodingTool.y(2)));
		addSlot(new ResultSlot(blockEntity, 4, CodingTool.x(4), CodingTool.y(2)));
		blockEntity.onOpen(player);
	}

	/*
	 * 以下是ScreenHandler的方法
	 */
	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		boolean clicked = false;
		if (id == AllInOneMachineScreen.TEMPERATURE_BUTTON_SYNC_ID) {
			blockEntity.shiftTemperature();
			clicked = true;

		} else if (id == AllInOneMachineScreen.PRESSURE_BUTTON_SYNC_ID) {
			blockEntity.shiftPressure();
			clicked = true;
		}
		return super.onButtonClick(player, id) || clicked;

	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack = slot.getStack();
			if (index >= 36 && index < 36 + blockEntity.size()) {
				this.insertItem(itemStack, 0, 36, true);
			} else if (index < 36) {
				if (itemStack.getItem() == Items.EXPERIENCE_BOTTLE)
					insertItem(itemStack, 36, 37, false);
				else {
					for (int i = 0; i < 2; i++) {
						if (getSlot(i + 37).getStack().isEmpty()) {
							if (!ItemStack.areItemsEqual(itemStack, getSlot((i ^ 1) + 37).getStack())) {
								insertItem(itemStack, i + 37, i + 38, false);
							}
						} else {
							insertItem(itemStack, i + 37, i + 38, false);
						}
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	/*
	 * 以下是自定义方法
	 */

}
