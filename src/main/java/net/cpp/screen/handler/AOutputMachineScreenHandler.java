package net.cpp.screen.handler;

import net.cpp.block.entity.AOutputMachineBlockEntity;
import net.cpp.screen.OutputDirectionButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;

public abstract class AOutputMachineScreenHandler extends AMachineScreenHandler {
	protected PlayerInventory playerInventory;
	public final AOutputMachineBlockEntity blockEntity;

	public AOutputMachineScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, AOutputMachineBlockEntity blockEntity) {
		super(type, syncId, playerInventory, blockEntity);
		this.blockEntity = blockEntity;
		addProperties(blockEntity.getPropertyDelegate());
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		if (blockEntity != null && id == OutputDirectionButton.SYNC_ID) {
			blockEntity.shiftOutputDir();
			return true;
		}
		return false;
	}
}
