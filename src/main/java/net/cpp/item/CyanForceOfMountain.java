package net.cpp.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.cpp.api.CppChat.say;

public class CyanForceOfMountain extends Item {
    public CyanForceOfMountain(Settings settings) {
        super(settings);
    }

    private static final ImmutableList<Block> canClear = ImmutableList.of(Blocks.DIRT, Blocks.COARSE_DIRT,
            Blocks.GRASS_PATH, Blocks.FARMLAND, Blocks.PODZOL, Blocks.GRASS_BLOCK, Blocks.MYCELIUM, Blocks.STONE,
            Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.GRAVEL, Blocks.SAND, Blocks.SANDSTONE,
            Blocks.NETHERRACK, Blocks.BLACKSTONE);

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            if (user.isCreative()) {
                fill(world, user);
                return TypedActionResult.success(user.getStackInHand(hand));
            }else if (user.experienceLevel >= 1) {
                user.addExperience(-9);
                fill(world, user);
                return TypedActionResult.success(user.getStackInHand(hand));
            }else {
                say(user, new TranslatableText("chat.cpp.exp.less"));
                return TypedActionResult.pass(user.getStackInHand(hand));
            }

        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    private static void fill(World world, PlayerEntity user) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                BlockPos pos = new BlockPos(user.getPos().add(i, -1, j));

                if (world.getBlockState(pos).getBlock() == Blocks.AIR) {
                    world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                }
            }

        }

        for (int i = (int) user.getPos().y; i <=255; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = -2; k <= 2; k++) {
                    BlockPos pos = new BlockPos(user.getPos().add(j, i - (int)user.getPos().y, k));
                    if (canClear.contains(world.getBlockState(pos).getBlock())) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                }
            }
        }

    }
}
