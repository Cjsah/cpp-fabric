package cpp.entity;

import java.util.ArrayList;
import java.util.List;

import cpp.api.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class GolemMinerEntity extends AMovingGolemEntity {

	public GolemMinerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void work() {
		BlockState blockState = getLandingBlockState();
		if (!world.isClient) {
			if (!CONTROLS.contains(blockState.getBlock()) && Utils.canHarvest(getMainHandStack(), blockState, world, getBlockPos())) {
				List<ItemStack> droppeds = new ArrayList<>();
				Utils.excavate((ServerWorld) world, this, getBlockPos(), droppeds);
				listMerge(droppeds);
			}
			experience = Utils.mend(mainHandStack, experience);
		}
	}

}
