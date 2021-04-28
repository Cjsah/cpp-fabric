package cpp.item;

import cpp.entity.AGolemEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class Golem extends Item {
	private final EntityType<? extends AGolemEntity> type;

	public Golem(Settings settings, EntityType<? extends AGolemEntity> type) {
		super(settings);
		this.type = type;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		ItemPlacementContext itemPlacementContext = new ItemPlacementContext(context);
		BlockPos blockPos = itemPlacementContext.getBlockPos();
		ItemStack itemStack = context.getStack();
		if (world instanceof ServerWorld) {
			ServerWorld serverWorld = (ServerWorld) world;
			AGolemEntity entity = type.create(serverWorld, itemStack.getTag(), null, context.getPlayer(), blockPos, SpawnReason.SPAWN_EGG, false, false);
			if (entity == null)
				return ActionResult.FAIL;
			entity.setMainHandStack(ItemStack.fromNbt(itemStack.getSubTag("tool")));
			serverWorld.spawnEntityAndPassengers(entity);
			world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
			world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, entity);
		}
		itemStack.decrement(1);
		return ActionResult.success(world.isClient);
	}
}
