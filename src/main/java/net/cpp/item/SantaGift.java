package net.cpp.item;

import net.cpp.api.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SantaGift extends Item {
	public SantaGift(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack item = user.getStackInHand(hand);
		if (!world.isClient) {
			ItemStack itemStack = world.getServer().getLootManager().getTable(new Identifier("cpp:items/santa_gift")).generateLoot((new LootContext.Builder((ServerWorld) world)).random(world.random).build(LootContextTypes.EMPTY)).get(0);
			Utils.give(user, itemStack);
			item.decrement(1);
			user.incrementStat(Stats.USED.getOrCreateStat(this));
		}
		return TypedActionResult.success(item);
	}
}
