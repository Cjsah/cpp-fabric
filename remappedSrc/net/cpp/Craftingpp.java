package net.cpp;

import net.cpp.api.CppChain;
import net.cpp.init.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class Craftingpp implements ModInitializer {

	public static final ItemGroup CPP_GROUP_MACHINE = FabricItemGroupBuilder.create(new Identifier("cpp:machine_title")).icon(() -> new ItemStack(CppBlocks.CRAFTING_MACHINE)).build();
	public static final ItemGroup CPP_GROUP_MISC = FabricItemGroupBuilder.create(new Identifier("cpp:misc_title")).icon(() -> new ItemStack(CppItems.ENCHANTED_IRON)).build();
	public static final ItemGroup CPP_GROUP_TOOL = FabricItemGroupBuilder.create(new Identifier("cpp:tool_title")).icon(() -> new ItemStack(CppItems.BROOM)).build();
	public static final ItemGroup CPP_GROUP_FOOD = FabricItemGroupBuilder.create(new Identifier("cpp:food_title")).icon(() -> new ItemStack(CppItems.CITRUS)).build();
	public static final ItemGroup CPP_GROUP_PLANT = FabricItemGroupBuilder.create(new Identifier("cpp:plant_title")).icon(() -> new ItemStack(CppItems.ORE_SAPLING)).build();

	@Override
	public void onInitialize() {
		CppBlocks.register();
		CppItems.register();
		CppBlockEntities.register();
		CppScreenHandler.register();
		CppRecipes.register();
		CppStats.register();
		CppEffects.register();
		CppChainMap.register();

		// 连环药水效果
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
			StatusEffectInstance effect = player.getStatusEffect(CppEffects.CHAIN);
			if (effect != null && CppChainMap.ChainBlocks.contains(state.getBlock()) && CppChainMap.ChainTools.contains(player.getMainHandStack().getItem())) {
				CppChain.chain(world, (ServerPlayerEntity) player, pos, state.getBlock());
			}
		});
	}
}