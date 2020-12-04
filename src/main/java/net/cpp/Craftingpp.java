package net.cpp;

import com.google.common.collect.ImmutableList;
import net.cpp.api.CppChain;
import net.cpp.init.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
		CppEvents.register();

		ImmutableList<Block> list = ImmutableList.of(Blocks.DIAMOND_ORE, Blocks.IRON_ORE, Blocks.EMERALD_ORE);
		// 连环药水效果
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
			StatusEffectInstance effect = player.getStatusEffect(CppEffects.CHAIN);
			if (effect != null && list.contains(state.getBlock()) && player.getMainHandStack().getItem() == Items.DIAMOND_PICKAXE) {
				CppChain.chain(world, player, pos, state.getBlock());
			}
		});
	}
}
