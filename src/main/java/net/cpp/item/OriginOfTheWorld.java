package net.cpp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OriginOfTheWorld extends Item {
	public OriginOfTheWorld(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack item = user.getStackInHand(hand);
		if (!world.isClient) {
			BlockPos pos = ((ServerWorld) world).getSpawnPos();
			if (user.isCreative()) {
				user.teleport(pos.getX(), pos.getY(), pos.getZ(), true);
				user.incrementStat(Stats.USED.getOrCreateStat(this));
				return TypedActionResult.success(item);
			} else if (user.totalExperience >= 32) {
				user.teleport(pos.getX(), pos.getY(), pos.getZ(), true);
				user.addExperience(-32);
				user.incrementStat(Stats.USED.getOrCreateStat(this));
				return TypedActionResult.success(item);
			}
		}
		return TypedActionResult.pass(item);
	}
}
