package net.cpp.item;

import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Item;

public class BreakHand extends Item implements ITickableInItemFrame {

	public BreakHand(Settings settings) {
		super(settings);
	}

	@Override
	public boolean tick(ItemFrameEntity itemFrameEntity) {
		return itemFrameEntity.world.breakBlock(itemFrameEntity.getBlockPos().up(1), true);
	}

}
