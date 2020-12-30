package net.cpp.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class Muffler extends Item {

	public Muffler(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		user.incrementStat(Stats.USED.getOrCreateStat(this));
		if (!world.isClient) {
			double dis = 16;
			int cnt = 0;
			for (Entity entity : ((ServerWorld) world).getOtherEntities(null, new Box(user.getPos(), user.getPos()).expand(dis), entity -> user.getPos().isInRange(entity.getPos(), dis))) {
				if (!(entity instanceof PlayerEntity)) {
					entity.setSilent(true);
					cnt++;
				}
			}
			user.sendMessage(new TranslatableText("tooltip.cpp.erasure", cnt), true);
		}
		return TypedActionResult.success(stack);
	}
}
