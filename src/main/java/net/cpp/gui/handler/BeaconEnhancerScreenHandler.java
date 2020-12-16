package net.cpp.gui.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.cpp.block.entity.BeaconEnhancerBlockEntity;
import net.cpp.gui.screen.BeaconEnhancerScreen;
import net.cpp.init.CppScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class BeaconEnhancerScreenHandler extends ScreenHandler {
	public final BeaconEnhancerBlockEntity blockEntity;

	public BeaconEnhancerScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new BeaconEnhancerBlockEntity());
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
		switch (id) {
		case BeaconEnhancerScreen.PLAYER_EFFECT_BUTTON_SYNC_ID:
			blockEntity.shiftPlayerEffect();
			clicked = true;
			break;
		case BeaconEnhancerScreen.MOB_EFFECT_BUTTON_SYNC_ID:
			blockEntity.shiftMobEffect();
			clicked = true;
			break;
		case BeaconEnhancerScreen.ONLY_ADVERSE_BUTTON_SYNC_ID:
			blockEntity.shiftOnlyAdverse();
			clicked = true;
			break;
		default:
			break;
		}
		return clicked || super.onButtonClick(player, id);
	}
}
