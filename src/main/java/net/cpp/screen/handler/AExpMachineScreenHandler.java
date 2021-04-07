package net.cpp.screen.handler;

import net.cpp.api.Utils;
import net.cpp.block.entity.AExpMachineBlockEntity;
import net.cpp.screen.ExpTankButton;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;

public abstract class AExpMachineScreenHandler extends AOutputMachineScreenHandler {
	public final AExpMachineBlockEntity blockEntity;

	public AExpMachineScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, AExpMachineBlockEntity blockEntity) {
		super(type, syncId, playerInventory, blockEntity);
		this.blockEntity = blockEntity;
		addSlot(new ExperienceBottleSlot(blockEntity, 0, Utils.x(7), Utils.y(0)));
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		boolean clicked = false;
		if (id == ExpTankButton.SYNC_ID && !blockEntity.getWorld().isClient) {
			ExperienceOrbEntity.spawn((ServerWorld) blockEntity.getWorld(), player.getPos(), blockEntity.getExpStorage());
			blockEntity.setExpStorage(0);
		}
		return clicked || super.onButtonClick(player, id);
	}

}
