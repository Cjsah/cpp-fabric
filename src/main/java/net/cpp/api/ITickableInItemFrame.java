package net.cpp.api;

import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.util.math.Vec3d;

public interface ITickableInItemFrame {
	boolean tick(ItemFrameEntity itemFrameEntity);

	static Vec3d getPos(ItemFrameEntity itemFrameEntity) {
		return itemFrameEntity.getPos().add(itemFrameEntity.getRotationVector().multiply(itemFrameEntity.getRotation()));
	}
}
