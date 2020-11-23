package net.cjsah.cpp.init;

import net.cjsah.cpp.block.CraftingMachineBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.cjsah.cpp.Craftingpp.CPP_GROUP;

public class CppBlocks {
    public static Block CRAFTING_MACHINE_BLOCK = Registry.register(Registry.BLOCK, new Identifier("cpp", "crafting_machine"), new CraftingMachineBlock());;

    public static void register() {

        Registry.register(Registry.ITEM, new Identifier("cpp", "crafting_machine"), new BlockItem(CRAFTING_MACHINE_BLOCK, new Item.Settings().group(CPP_GROUP)));
    }

}
