package net.cpp.item;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class OrangeForceOfDirt extends Item {

    public OrangeForceOfDirt(Settings settings) {
        super(settings);
    }

    private static final ImmutableList<Block> canClear = ImmutableList.of(Blocks.DIRT, Blocks.COARSE_DIRT,
            Blocks.DIRT_PATH, Blocks.FARMLAND, Blocks.PODZOL, Blocks.GRASS_BLOCK, Blocks.MYCELIUM, Blocks.STONE,
            Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.GRAVEL, Blocks.SAND, Blocks.SANDSTONE,
            Blocks.NETHERRACK, Blocks.BLACKSTONE);

    @Override
    @Environment(EnvType.CLIENT)
    public Text getName(ItemStack stack) {
        return new TranslatableText(this.getTranslationKey()).formatted(Formatting.GOLD);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack item = user.getStackInHand(hand);
        if (!world.isClient) {
            BlockPos blockPos = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY).getBlockPos();
            if (canClear.contains(world.getBlockState(blockPos).getBlock())) {
                world.breakBlock(blockPos,true, user);
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(item);
            }
        }
        return TypedActionResult.pass(item);
    }
}
