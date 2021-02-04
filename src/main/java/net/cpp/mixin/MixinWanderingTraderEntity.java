package net.cpp.mixin;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static net.minecraft.item.Items.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradeOffers.Factory;
import net.minecraft.world.World;

@Mixin(WanderingTraderEntity.class)
public abstract class MixinWanderingTraderEntity extends MerchantEntity {
	private static final List<List<Function<Random, TradeOffer>>> TRADES = Lists.newLinkedList();

	public MixinWanderingTraderEntity(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("RETURN"), method = { "<init>" })
	public void init(CallbackInfo info) {
		if (!world.isClient) {
			offers.clear();

		}
	}

	static {
		List<Function<Random, TradeOffer>> list = Lists.newArrayList();
		for (Item item : new Item[] {}) {

		}
	}
}
