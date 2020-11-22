package net.cjsah.cpp;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CraftingppMod implements ModInitializer {

	@Override
	public void onInitialize() {

		Registry.register(Registry.BLOCK, new Identifier("cpp", "crafting_machine"), CRAFTING_MACHINE_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("cpp", "crafting_machine"), new BlockItem(CRAFTING_MACHINE_BLOCK, new Item.Settings().group(CPP_GROUP)));

	}
	
	public static final Block CRAFTING_MACHINE_BLOCK = new Block(FabricBlockSettings.of(Material.WOOD).strength(3.0F, 4.8F).sounds(BlockSoundGroup.WOOD));

//	// 创造模式物品栏
	public static final ItemGroup CPP_GROUP = FabricItemGroupBuilder.create(
			new Identifier("cpp", "title"))
			.icon(() -> new ItemStack(CRAFTING_MACHINE_BLOCK)).appendItems(stacks -> {
				stacks.add(new ItemStack(CRAFTING_MACHINE_BLOCK));
			}).build();
}
