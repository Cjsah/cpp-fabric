package net.cpp.block;

import java.lang.reflect.Field;
import java.util.Map;

import net.cpp.init.CppDimensionTypes;
import net.cpp.init.CppWorlds;
import net.fabricmc.fabric.impl.dimension.FabricDimensionInternals;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class FlowerPortalBlock extends Block {

	public FlowerPortalBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient) {
//			CppDimensionTypes.register(world.getServer());
			if (CppDimensionTypes.getFlower() != null) {
				ServerWorld world2;
				if (world.getDimension() == CppDimensionTypes.getFlower()) {
					world2 = world.getServer().getWorld(World.OVERWORLD);
				} else {
					world2 = world.getServer().getWorld(CppWorlds.FLOWER_KEY);
				}
				if (world2 != null) {
					FabricDimensionInternals.changeDimension(entity, world2, new TeleportTarget(entity.getPos(), entity.getVelocity(), entity.yaw, entity.pitch));
				}
			}else {
				LOGGER.error("无法找到花世界维度");
			}
		}
	}
}
