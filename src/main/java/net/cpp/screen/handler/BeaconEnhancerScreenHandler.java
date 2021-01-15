package net.cpp.screen.handler;

import net.cpp.block.entity.BeaconEnhancerBlockEntity;
import net.cpp.init.CppScreenHandler;
import net.cpp.screen.BeaconEnhancerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class BeaconEnhancerScreenHandler extends ScreenHandler {
	public final BeaconEnhancerBlockEntity blockEntity;

	public BeaconEnhancerScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new BeaconEnhancerBlockEntity(playerInventory.player.getBlockPos(), playerInventory.player.getBlockState()));
	}

	public BeaconEnhancerScreenHandler(int syncId, PlayerInventory playerInventory, BeaconEnhancerBlockEntity blockEntity) {
		super(CppScreenHandler.BEACON_ENHANCER, syncId);
		this.blockEntity = blockEntity;
		for (int m = 0; m < 3; ++m) {
			for (int l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
			}
		}
		for (int m = 0; m < 9; ++m) {
			this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
		}
		addProperties(blockEntity.propertyDelegate);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		boolean clicked = false;
		if (id == BeaconEnhancerScreen.PLAYER_EFFECT_BUTTON_SYNC_ID) {
			blockEntity.shiftPlayerEffect();
			clicked = true;
		} else if (id == BeaconEnhancerScreen.MOB_EFFECT_BUTTON_SYNC_ID) {
			blockEntity.shiftMobEffect();
			clicked = true;
		} else if (id == BeaconEnhancerScreen.ONLY_ADVERSE_BUTTON_SYNC_ID) {
			blockEntity.shiftOnlyAdverse();
			clicked = true;
		}
		return clicked || super.onButtonClick(player, id);
	}
}
