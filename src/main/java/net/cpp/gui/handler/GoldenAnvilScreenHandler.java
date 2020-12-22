package net.cpp.gui.handler;

import net.cpp.api.CodingTool;
import net.cpp.block.entity.AExpMachineBlockEntity;
import net.cpp.block.entity.GoldenAnvilBlockEntity;
import net.cpp.block.entity.ItemProcessorBlockEntity;
import net.cpp.block.entity.TradeMachineBlockEntity;
import net.cpp.init.CppItems;
import net.cpp.init.CppScreenHandler;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class GoldenAnvilScreenHandler extends AExpMachineScreenHandler {
	public final GoldenAnvilBlockEntity blockEntity;

	public GoldenAnvilScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new GoldenAnvilBlockEntity(playerInventory.player.getBlockPos(), playerInventory.player.getBlockState()));
	}

	public GoldenAnvilScreenHandler(int syncId, PlayerInventory playerInventory, GoldenAnvilBlockEntity blockEntity) {
		super(CppScreenHandler.GOLDEN_ANVIL, syncId, playerInventory, blockEntity);
		this.blockEntity = blockEntity;

		addSlot(new Slot(blockEntity, 1, CodingTool.x(1), CodingTool.y(0)));
		addSlot(new ResultSlot(blockEntity, 2, CodingTool.x(3), CodingTool.y(0)));
		addSlot(new ResultSlot(blockEntity, 3, CodingTool.x(2), CodingTool.y(2)));
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack = slot.getStack();
			if (index >= 36 && index < 36 + blockEntity.getInvStackList().size()) {
				this.insertItem(itemStack, 0, 36, true);
			} else if (index < 36) {
				if (itemStack.getItem() == Items.EXPERIENCE_BOTTLE)
					insertItem(itemStack, 36, 37, false);
				else {
					
				}
			}
		}
		return ItemStack.EMPTY;
	}
}
