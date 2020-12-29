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
import net.minecraft.world.World;

public class YellowForceOfEarth extends Item {

    private final ImmutableList<Block> canFillFluids = ImmutableList.of(Blocks.WATER, Blocks.LAVA);

    public YellowForceOfEarth(Settings settings) {
        super(settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Text getName(ItemStack stack) {
        return new TranslatableText(this.getTranslationKey()).formatted(Formatting.GOLD);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack item = user.getStackInHand(hand);
        if (!world.isClient) {
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            for (int i = -7; i <= 7; i++) {
                for (int j = -2; j <= 0; j++) {
                    for (int k = -7; k <= 7; k++) {
                        BlockPos pos = new BlockPos(user.getPos().add(i, j, k));
                        if (canFillFluids.contains(world.getBlockState(pos).getBlock())) {
                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
            return TypedActionResult.success(item);
        }
        return TypedActionResult.pass(item);
    }
}
