package net.cpp.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class CppChainMap {
    public static final Map<Block, Item> BreakResult = new HashMap<>();
    public static final Map<Block, String> BreakTools = new HashMap<>();
    public static final Map<String, ImmutableList<Item>> ToolType = new HashMap<>();
    public static final ArrayList<Item> ChainTools = new ArrayList<>();
    public static final ArrayList<Block> ChainBlocks = new ArrayList<>();

    static {
        ToolType.put("axe", ImmutableList.of(
                Items.WOODEN_AXE,
                Items.STONE_AXE,
                Items.GOLDEN_AXE,
                Items.IRON_AXE,
                Items.DIAMOND_AXE,
                Items.NETHERITE_AXE
        ));
        ToolType.put("pickaxe", ImmutableList.of(
                Items.WOODEN_PICKAXE,
                Items.STONE_PICKAXE,
                Items.GOLDEN_PICKAXE,
                Items.IRON_PICKAXE,
                Items.DIAMOND_PICKAXE,
                Items.NETHERITE_PICKAXE
        ));
        ToolType.put("shovel", ImmutableList.of(
                Items.WOODEN_SHOVEL,
                Items.STONE_SHOVEL,
                Items.GOLDEN_SHOVEL,
                Items.IRON_SHOVEL,
                Items.DIAMOND_SHOVEL,
                Items.NETHERITE_SHOVEL
        ));
        ToolType.put("hoe", ImmutableList.of(
                Items.WOODEN_HOE,
                Items.STONE_HOE,
                Items.GOLDEN_HOE,
                Items.IRON_HOE,
                Items.DIAMOND_HOE,
                Items.NETHERITE_HOE
        ));


        BreakResult.put(Blocks.COAL_ORE, Items.COAL);
        BreakResult.put(Blocks.DIAMOND_ORE, Items.DIAMOND);
        BreakResult.put(Blocks.LAPIS_ORE, Items.LAPIS_LAZULI);
        BreakResult.put(Blocks.EMERALD_ORE, Items.EMERALD);
        BreakResult.put(Blocks.REDSTONE_ORE, Items.REDSTONE);


        BreakTools.put(Blocks.OAK_LOG, "axe");
        BreakTools.put(Blocks.SPRUCE_LOG, "axe");
        BreakTools.put(Blocks.BIRCH_LOG, "axe");
        BreakTools.put(Blocks.JUNGLE_LOG, "axe");
        BreakTools.put(Blocks.ACACIA_LOG, "axe");
        BreakTools.put(Blocks.DARK_OAK_LOG, "axe");
        BreakTools.put(Blocks.CRIMSON_STEM, "axe");
        BreakTools.put(Blocks.WARPED_STEM, "axe");
        BreakTools.put(Blocks.PUMPKIN, "axe");
        BreakTools.put(Blocks.BROWN_MUSHROOM_BLOCK, "axe");
        BreakTools.put(Blocks.RED_MUSHROOM_BLOCK, "axe");
        BreakTools.put(Blocks.MUSHROOM_STEM, "axe");

        BreakTools.put(Blocks.COAL_ORE, "pickaxe");
        BreakTools.put(Blocks.IRON_ORE, "pickaxe");
        BreakTools.put(Blocks.GOLD_ORE, "pickaxe");
        BreakTools.put(Blocks.DIAMOND_ORE, "pickaxe");
        BreakTools.put(Blocks.EMERALD_ORE, "pickaxe");
        BreakTools.put(Blocks.LAPIS_ORE, "pickaxe");
        BreakTools.put(Blocks.NETHER_GOLD_ORE, "pickaxe");
        BreakTools.put(Blocks.NETHER_QUARTZ_ORE, "pickaxe");
        BreakTools.put(Blocks.REDSTONE_ORE, "pickaxe");
        BreakTools.put(Blocks.GLOWSTONE, "pickaxe");
        BreakTools.put(Blocks.OBSIDIAN, "pickaxe");
        BreakTools.put(Blocks.ANCIENT_DEBRIS, "pickaxe");

        BreakTools.put(Blocks.SAND, "shovel");
        BreakTools.put(Blocks.RED_SAND, "shovel");
        BreakTools.put(Blocks.GRAVEL, "shovel");
        BreakTools.put(Blocks.CLAY, "shovel");
        BreakTools.put(Blocks.SOUL_SAND, "shovel");
        BreakTools.put(Blocks.SOUL_SOIL, "shovel");

        BreakTools.put(Blocks.NETHER_WART_BLOCK, "hoe");
        BreakTools.put(Blocks.WARPED_WART_BLOCK, "hoe");
        BreakTools.put(Blocks.SHROOMLIGHT, "hoe");

        ChainTools.addAll(ToolType.get("axe"));
        ChainTools.addAll(ToolType.get("pickaxe"));
        ChainTools.addAll(ToolType.get("shovel"));
        ChainTools.addAll(ToolType.get("hoe"));

        ChainBlocks.addAll(ImmutableList.of(
                Blocks.OAK_LOG,
                Blocks.SPRUCE_LOG,
                Blocks.BIRCH_LOG,
                Blocks.JUNGLE_LOG,
                Blocks.ACACIA_LOG,
                Blocks.DARK_OAK_LOG,
                Blocks.CRIMSON_STEM,
                Blocks.WARPED_STEM,
                Blocks.PUMPKIN,
                Blocks.BROWN_MUSHROOM_BLOCK,
                Blocks.RED_MUSHROOM_BLOCK,
                Blocks.MUSHROOM_STEM,
                Blocks.COAL_ORE,
                Blocks.IRON_ORE,
                Blocks.GOLD_ORE,
                Blocks.DIAMOND_ORE,
                Blocks.EMERALD_ORE,
                Blocks.LAPIS_ORE,
                Blocks.NETHER_GOLD_ORE,
                Blocks.NETHER_QUARTZ_ORE,
                Blocks.REDSTONE_ORE,
                Blocks.GLOWSTONE,
                Blocks.OBSIDIAN,
                Blocks.ANCIENT_DEBRIS,
                Blocks.SAND,
                Blocks.RED_SAND,
                Blocks.GRAVEL,
                Blocks.CLAY,
                Blocks.SOUL_SAND,
                Blocks.SOUL_SOIL,
                Blocks.NETHER_WART_BLOCK,
                Blocks.WARPED_WART_BLOCK,
                Blocks.SHROOMLIGHT
        ));


    }
    public static void register() {}

}
