package net.cpp.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class RedForceOfFire extends Item {

    private static final Map<Block, Item> furnaceResult = new HashMap<>();

    public RedForceOfFire(Settings settings) {
        super(settings);
        this.register();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack item = user.getStackInHand(hand);
        if (!world.isClient) {
            BlockPos pos = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY).getBlockPos();
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            if (furnaceResult.get(block) != null) {
                ItemEntity spawnItem = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(furnaceResult.get(block), block != Blocks.SEA_PICKLE ? 1 : blockState.get(SeaPickleBlock.PICKLES)));
                spawnItem.setToDefaultPickupDelay();
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                world.spawnEntity(spawnItem);
                return TypedActionResult.success(item);
            }
        }
        return TypedActionResult.pass(item);
    }

    private void register() {
        furnaceResult.put(Blocks.COBBLESTONE, Items.STONE);
        furnaceResult.put(Blocks.STONE, Items.SMOOTH_STONE);
        furnaceResult.put(Blocks.SAND, Items.GLASS);
        furnaceResult.put(Blocks.WET_SPONGE, Items.SPONGE);
        furnaceResult.put(Blocks.STONE_BRICKS, Items.CRACKED_STONE_BRICKS);
        furnaceResult.put(Blocks.DARK_OAK_LOG, Items.CHARCOAL);
        furnaceResult.put(Blocks.DARK_OAK_WOOD, Items.CHARCOAL);
        furnaceResult.put(Blocks.STRIPPED_DARK_OAK_LOG, Items.CHARCOAL);
        furnaceResult.put(Blocks.STRIPPED_DARK_OAK_WOOD, Items.CHARCOAL);
        furnaceResult.put(Blocks.OAK_LOG, Items.CHARCOAL);
        furnaceResult.put(Blocks.OAK_WOOD, Items.CHARCOAL);
        furnaceResult.put(Blocks.STRIPPED_OAK_LOG, Items.CHARCOAL);
        furnaceResult.put(Blocks.STRIPPED_OAK_WOOD, Items.CHARCOAL);
        furnaceResult.put(Blocks.ACACIA_LOG, Items.CHARCOAL);
        furnaceResult.put(Blocks.ACACIA_WOOD, Items.CHARCOAL);
        furnaceResult.put(Blocks.STRIPPED_ACACIA_LOG, Items.CHARCOAL);
        furnaceResult.put(Blocks.STRIPPED_ACACIA_WOOD, Items.CHARCOAL);
        furnaceResult.put(Blocks.BIRCH_LOG, Items.CHARCOAL);
        furnaceResult.put(Blocks.BIRCH_WOOD, Items.CHARCOAL);
        furnaceResult.put(Blocks.STRIPPED_BIRCH_LOG, Items.CHARCOAL);
        furnaceResult.put(Blocks.STRIPPED_BIRCH_WOOD, Items.CHARCOAL);
        furnaceResult.put(Blocks.JUNGLE_LOG, Items.CHARCOAL);
        furnaceResult.put(Blocks.JUNGLE_WOOD, Items.CHARCOAL);
        furnaceResult.put(Blocks.STRIPPED_JUNGLE_LOG, Items.CHARCOAL);
        furnaceResult.put(Blocks.STRIPPED_JUNGLE_WOOD, Items.CHARCOAL);
        furnaceResult.put(Blocks.SPRUCE_LOG, Items.CHARCOAL);
        furnaceResult.put(Blocks.SPRUCE_WOOD, Items.CHARCOAL);
        furnaceResult.put(Blocks.STRIPPED_SPRUCE_LOG, Items.CHARCOAL);
        furnaceResult.put(Blocks.STRIPPED_SPRUCE_WOOD, Items.CHARCOAL);
        furnaceResult.put(Blocks.COAL_ORE, Items.COAL);
        furnaceResult.put(Blocks.IRON_ORE, Items.IRON_INGOT);
        furnaceResult.put(Blocks.GOLD_ORE, Items.GOLD_INGOT);
        furnaceResult.put(Blocks.DIAMOND_ORE, Items.DIAMOND);
        furnaceResult.put(Blocks.LAPIS_ORE, Items.LAPIS_LAZULI);
        furnaceResult.put(Blocks.EMERALD_ORE, Items.EMERALD);
        furnaceResult.put(Blocks.REDSTONE_ORE, Items.REDSTONE);
        furnaceResult.put(Blocks.COPPER_ORE, Items.COPPER_INGOT);
        furnaceResult.put(Blocks.ANCIENT_DEBRIS, Items.NETHERITE_SCRAP);
        furnaceResult.put(Blocks.NETHER_QUARTZ_ORE, Items.QUARTZ);
        furnaceResult.put(Blocks.CLAY, Items.BRICK);
        furnaceResult.put(Blocks.CACTUS, Items.GREEN_DYE);
        furnaceResult.put(Blocks.SEA_PICKLE, Items.LIME_DYE);
        furnaceResult.put(Blocks.WHITE_TERRACOTTA, Items.WHITE_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.ORANGE_TERRACOTTA, Items.ORANGE_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.MAGENTA_TERRACOTTA, Items.MAGENTA_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.LIGHT_BLUE_TERRACOTTA, Items.LIGHT_BLUE_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.YELLOW_TERRACOTTA, Items.YELLOW_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.LIME_TERRACOTTA, Items.LIME_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.PINK_TERRACOTTA, Items.PINK_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.GRAY_TERRACOTTA, Items.GRAY_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.LIGHT_GRAY_TERRACOTTA, Items.LIGHT_GRAY_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.CYAN_TERRACOTTA, Items.CYAN_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.PURPLE_TERRACOTTA, Items.PURPLE_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.BLUE_TERRACOTTA, Items.BLUE_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.BROWN_TERRACOTTA, Items.BROWN_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.GREEN_TERRACOTTA, Items.GREEN_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.RED_TERRACOTTA, Items.RED_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.BLACK_TERRACOTTA, Items.BLACK_GLAZED_TERRACOTTA);
        furnaceResult.put(Blocks.QUARTZ_BLOCK, Items.SMOOTH_QUARTZ);
        furnaceResult.put(Blocks.NETHERRACK, Items.NETHER_BRICK);
        furnaceResult.put(Blocks.NETHER_BRICKS, Items.CRACKED_NETHER_BRICKS);
        furnaceResult.put(Blocks.POLISHED_BLACKSTONE_BRICKS, Items.CRACKED_POLISHED_BLACKSTONE_BRICKS);
    }
}
