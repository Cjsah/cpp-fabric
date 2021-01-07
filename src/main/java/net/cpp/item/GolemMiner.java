package net.cpp.item;

import net.cpp.entity.GolemMinerEntity;
import net.cpp.init.CppEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class GolemMiner extends Item {

	public GolemMiner(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		Direction direction = context.getSide();
		if (direction == Direction.DOWN) {
			return ActionResult.FAIL;
		} else {
			World world = context.getWorld();
			ItemPlacementContext itemPlacementContext = new ItemPlacementContext(context);
			BlockPos blockPos = itemPlacementContext.getBlockPos();
			ItemStack itemStack = context.getStack();
			Vec3d vec3d = Vec3d.ofBottomCenter(blockPos);
			Box box = CppEntities.GOLEM_MINER.getDimensions().method_30231(vec3d.getX(), vec3d.getY(), vec3d.getZ());
			if (world.isSpaceEmpty((Entity) null, box, (entity) -> {
				return true;
			}) && world.getOtherEntities((Entity) null, box).isEmpty()) {
				if (world instanceof ServerWorld) {
					ServerWorld serverWorld = (ServerWorld) world;
					GolemMinerEntity entity = (GolemMinerEntity) CppEntities.GOLEM_MINER.create(serverWorld, itemStack.getTag(), (Text) null, context.getPlayer(), blockPos, SpawnReason.SPAWN_EGG, true, true);
					if (entity == null) {
						return ActionResult.FAIL;
					}
					entity.setMainHandStack(ItemStack.fromTag(itemStack.getSubTag("tool")));
					float f = (float) MathHelper.floor((MathHelper.wrapDegrees(context.getPlayerYaw() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
					entity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), f, 0.0F);
					serverWorld.spawnEntityAndPassengers(entity);
					world.playSound((PlayerEntity) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
					world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, entity);
				}

				itemStack.decrement(1);
				return ActionResult.success(world.isClient);
			} else {
				return ActionResult.FAIL;
			}
		}
	}
}
