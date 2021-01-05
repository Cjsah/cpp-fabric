package net.cpp.item;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ToughenHand extends Item implements ITickableInItemFrame {

	public ToughenHand(Settings settings) {
		super(settings);
	}

	@Override
	public boolean tick(ItemFrameEntity itemFrameEntity) {
		World world = itemFrameEntity.world;
		if (world.getTime() % 100 == 0) {
			Vec3d pos = itemFrameEntity.getPos();
			for (ItemEntity itemEntity : world.getEntitiesByClass(ItemEntity.class, new Box(pos, pos).expand(16), itemEntity -> itemEntity.getStack().isOf(Items.EGG))) {
				itemEntity.teleport(pos.x, pos.y, pos.z);
			}
		}
		return false;
	}
}
