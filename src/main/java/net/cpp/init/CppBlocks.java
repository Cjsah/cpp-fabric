package net.cpp.init;

import net.cpp.Craftingpp;
import net.cpp.block.AllInOneMachineBlock;
import net.cpp.block.CraftingMachineBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class CppBlocks {
	public static final Block CRAFTING_MACHINE = Registry.register(Registry.BLOCK,
			new Identifier("cpp:crafting_machine"), new CraftingMachineBlock());
	public static final Block ALL_IN_ONE_MACHINE = Registry.register(Registry.BLOCK,
			new Identifier("cpp:all_in_one_machine"), new AllInOneMachineBlock());

	public static void register() {
		registerBlockItem(CRAFTING_MACHINE);
		registerBlockItem(ALL_IN_ONE_MACHINE);
	}
	private static Item registerBlockItem(Block block) {
		return Registry.register(Registry.ITEM, Registry.BLOCK.getId(block),
				new BlockItem(block, new Item.Settings().group(Craftingpp.CPP_GROUP)));
	}
}
