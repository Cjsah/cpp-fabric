package net.cpp.init;

import net.cpp.block.CraftingMachineBlock;
import net.cpp.Craftingpp;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class CppBlocks {
    public static final Block CRAFTING_MACHINE = Registry.register(Registry.BLOCK, new Identifier("cpp:crafting_machine"), new CraftingMachineBlock());
    public static final Block ALL_IN_ONE_MACHINE = Registry.register(Registry.BLOCK, new Identifier("cpp:all_in_one_machine"), new CraftingMachineBlock());








    public static void register() {

        Registry.register(Registry.ITEM, new Identifier("cpp:crafting_machine"), new BlockItem(CRAFTING_MACHINE, new Item.Settings().group(Craftingpp.CPP_GROUP)));
        Registry.register(Registry.ITEM, new Identifier("cpp:all_in_one_machine"), new BlockItem(ALL_IN_ONE_MACHINE, new Item.Settings().group(Craftingpp.CPP_GROUP)));

    }

}
