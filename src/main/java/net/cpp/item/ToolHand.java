package net.cpp.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public abstract class ToolHand extends Item {

	public ToolHand(Settings settings) {
		super(settings);
	}

	public abstract boolean tickInItemFrame(ServerWorld world, BlockPos pos, ItemStack stack);
}
