package cpp.block;

import cpp.init.CppDimensionTypes;
import cpp.init.CppWorlds;
import net.fabricmc.fabric.impl.dimension.FabricDimensionInternals;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

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
					FabricDimensionInternals.changeDimension(entity, world2, new TeleportTarget(entity.getPos(), entity.getVelocity(), entity.getYaw(), entity.getPitch()));
				}
			}else {
				LOGGER.error("无法找到花世界维度");
			}
		}
	}
}
