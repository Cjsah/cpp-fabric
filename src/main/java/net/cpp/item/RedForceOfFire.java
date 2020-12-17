package net.cpp.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class RedForceOfFire extends Item {

    private final Map<Block, Block> furnaceResult = new HashMap<>();

    public RedForceOfFire(Settings settings) {
        super(settings);
        this.register();
    }

    private void register() {
        furnaceResult.put(Blocks.COBBLESTONE, Blocks.STONE);
        furnaceResult.put(Blocks.STONE, Blocks.SMOOTH_STONE);
        furnaceResult.put(Blocks.SAND, Blocks.GLASS);
        furnaceResult.put(Blocks.WET_SPONGE, Blocks.SPONGE);
        furnaceResult.put(Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS);
        furnaceResult.put(Blocks.WHITE_TERRACOTTA, Blocks.WHITE_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.ORANGE_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.MAGENTA_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.YELLOW_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.LIME_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.PINK_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.GRAY_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.CYAN_TERRACOTTA, Blocks.CYAN_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.PURPLE_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.BLUE_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.BROWN_TERRACOTTA, Blocks.BROWN_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.GREEN_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.RED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.BLACK_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.QUARTZ_BLOCK, Blocks.SMOOTH_QUARTZ);
        furnaceResult.put(Blocks.NETHER_BRICKS, Blocks.CRACKED_NETHER_BRICKS);
        furnaceResult.put(Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);

    }
}
