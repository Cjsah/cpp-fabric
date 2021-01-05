package net.cpp.item;

import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public interface ITickableInItemFrame {
	boolean tickInItemFrame(ServerWorld world, Vec3d pos, ItemStack stack);
}
