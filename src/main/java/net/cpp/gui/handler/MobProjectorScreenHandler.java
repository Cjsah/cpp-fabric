package net.cpp.gui.handler;

import net.cpp.api.CodingTool;
import net.cpp.block.entity.MobProjectorBlockEntity;
import net.cpp.init.CppScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class MobProjectorScreenHandler extends AExpMachineScreenHandler {
	private PlayerEntity player;
	public final MobProjectorBlockEntity blockEntity;

	public MobProjectorScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new MobProjectorBlockEntity(playerInventory.player.getBlockPos(),playerInventory.player.getBlockState()));
	}

	public MobProjectorScreenHandler(int syncId, PlayerInventory playerInventory, MobProjectorBlockEntity blockEntity) {
		super(CppScreenHandler.MOB_PROJECTOR, syncId, playerInventory, blockEntity);
		player = playerInventory.player;
		this.blockEntity = blockEntity;
		addSlot(new Slot(blockEntity, 1, CodingTool.x(4), CodingTool.y(0)));
		addSlot(new Slot(blockEntity, 2, CodingTool.x(4), CodingTool.y(1)));
		addSlot(new Slot(blockEntity, 3, CodingTool.x(4), CodingTool.y(2)));
		blockEntity.onOpen(player);
	}

	/*
	 * 以下是ScreenHandler的方法
	 */

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack = slot.getStack();
			if (index >= 36 && index < 42)
				insertItem(itemStack, 0, 36, true);
			else if (index < 36) {
				if (itemStack.getItem() == Items.EXPERIENCE_BOTTLE)
					insertItem(itemStack, 36, 37, false);
				else if (itemStack.getItem() == Items.EGG)
					insertItem(itemStack, 37, 38, false);
				else {
					for (int i = 0; i < 2; i++) {
						if (getSlot(i + 38).getStack().isEmpty()) {
							if (!ItemStack.areItemsEqual(itemStack, getSlot((i ^ 1) + 38).getStack())) {
								insertItem(itemStack, i + 38, i + 39, false);
							}
						} else {
							insertItem(itemStack, i + 38, i + 39, false);
						}
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}
}
