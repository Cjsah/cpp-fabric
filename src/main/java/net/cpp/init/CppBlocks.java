package net.cpp.init;

import net.cpp.block.AllInOneMachineBlock;
import net.cpp.block.CraftingMachineBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.cpp.Craftingpp.CPP_GROUP_MACHINE;

public final class CppBlocks {
    public static final Block CRAFTING_MACHINE = Registry.register(Registry.BLOCK, new Identifier("cpp:crafting_machine"), new CraftingMachineBlock());
    public static final Block ALL_IN_ONE_MACHINE = Registry.register(Registry.BLOCK, new Identifier("cpp:all_in_one_machine"), new AllInOneMachineBlock());
    public static final Block RARE_EARTH_GLASS = Registry.register(Registry.BLOCK, new Identifier("cpp:rare_earth_glass"), new Block(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL)));
    public static final Block REINFORCED_GLASS = Registry.register(Registry.BLOCK, new Identifier("cpp:reinforced_glass"), new Block(FabricBlockSettings.of(Material.STONE).requiresTool().strength(50.0F, 3600000.0F).sounds(BlockSoundGroup.METAL)));
    public static final Block MOON_STONE = Registry.register(Registry.BLOCK, new Identifier("cpp:moon_stone"), new Block(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL)));
    public static final Block SUN_STONE = Registry.register(Registry.BLOCK, new Identifier("cpp:sun_stone"), new Block(FabricBlockSettings.of(Material.METAL).requiresTool().strength(3.0F, 6.0F).sounds(BlockSoundGroup.METAL)));




    public static void register() {
        registerItem("crafting_machine",CRAFTING_MACHINE);
        registerItem("all_in_one_machine",ALL_IN_ONE_MACHINE);
    }

    private static void registerItem(String id,Block block) {
        Registry.register(Registry.ITEM, new Identifier("cpp:"+id), new BlockItem(block, new Item.Settings().group(CPP_GROUP_MACHINE)));
    }

}
