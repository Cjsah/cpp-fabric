package net.cpp.gui.handler;

import net.cpp.api.CodingTool;
import net.cpp.block.entity.AExpMachineBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;

public abstract class AExpMachineScreenHandler extends AMachineScreenHandler {
	public final AExpMachineBlockEntity blockEntity;

	public AExpMachineScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, AExpMachineBlockEntity blockEntity) {
		super(type, syncId, playerInventory, blockEntity);
		this.blockEntity = blockEntity;
		addSlot(new ExperienceBottleSlot(blockEntity, 0, CodingTool.x(6), CodingTool.y(0)));
	}

//	@Override
//	public ItemStack transferSlot(PlayerEntity player, int index) {
////		Slot slot = (Slot) this.slots.get(index);
////		if (slot != null && slot.hasStack()) {
////			ItemStack itemStack2 = slot.getStack();
////			if (index == 36) {
////				this.insertItem(itemStack2, 0, 36, true);
////			} else {
////				if (itemStack2.getItem() == Items.EXPERIENCE_BOTTLE && !insertItem(itemStack2, 36, 37, false)) {
////					super.transferSlot(player, index);
////				}
////			}
////		}
//		return super.transferSlot(player, index);
//	}

}
