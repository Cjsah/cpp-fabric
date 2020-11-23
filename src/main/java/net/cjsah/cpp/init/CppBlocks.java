package net.cjsah.cpp.init;

import net.cjsah.cpp.block.CraftingMachineBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.cjsah.cpp.Craftingpp.CPP_GROUP;

public class CppBlocks {
    public static final CraftingMachineBlock CRAFTING_MACHINE = new CraftingMachineBlock();

    private static void register(String id, Block block) {
        Registry.register(Registry.BLOCK, new Identifier("cpp", id), block);
        Registry.register(Registry.ITEM, new Identifier("cpp", id), new BlockItem(block, new Item.Settings().group(CPP_GROUP)));

    }

    public static void register() {
        register("crafting_machine", new CraftingMachineBlock());
    }

}
