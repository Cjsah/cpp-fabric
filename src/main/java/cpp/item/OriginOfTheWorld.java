package cpp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class OriginOfTheWorld extends Item {
	public OriginOfTheWorld(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack item = user.getStackInHand(hand);
		if (!world.isClient && (user.isCreative() || user.totalExperience >= 32)) {
			BlockPos pos = ((ServerWorld) world).getSpawnPos();
			ChunkPos chunkPos = new ChunkPos(pos);
			((ServerChunkManager)world.getChunkManager()).addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, user.getId());
			user.stopRiding();
			if (user.isSleeping()) user.wakeUp(true, true);
			((ServerPlayerEntity)user).networkHandler.requestTeleport(pos.getX(), pos.getY(), pos.getZ(), user.getYaw(), user.getPitch());
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			if (!user.isCreative()) user.addExperience(-32);
			return TypedActionResult.success(item);
		}
		return TypedActionResult.pass(item);
	}
}
