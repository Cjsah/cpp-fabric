package net.cpp.item;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.ItemTags;
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
		if (world.getTime() % 600 == 0) {
			Vec3d pos = ITickableInItemFrame.getPos(itemFrameEntity);
			for (SheepEntity sheep : world.getEntitiesByClass(SheepEntity.class, new Box(pos, pos).expand(16), sheep -> sheep.isShearable())) {
				sheep.sheared(SoundCategory.MASTER);
			}
			for (ItemEntity itemEntity : world.getEntitiesByClass(ItemEntity.class, new Box(pos, pos).expand(16), itemEntity -> itemEntity.getStack().isOf(Items.EGG) || itemEntity.getStack().isIn(ItemTags.WOOL))) {
				itemEntity.teleport(pos.x, pos.y, pos.z);
			}
		}
		return false;
	}
}
