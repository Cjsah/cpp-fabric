package net.cpp.item;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static net.cpp.api.CppChat.say;
import static net.cpp.api.CodingTool.getExperience;

public class CyanForceOfMountain extends Item {
    public CyanForceOfMountain(Settings settings) {
        super(settings);
    }

    private static final ImmutableList<Block> canClear = ImmutableList.of(Blocks.DIRT, Blocks.COARSE_DIRT,
            Blocks.DIRT_PATH, Blocks.FARMLAND, Blocks.PODZOL, Blocks.GRASS_BLOCK, Blocks.MYCELIUM, Blocks.STONE,
            Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.GRAVEL, Blocks.SAND, Blocks.SANDSTONE,
            Blocks.NETHERRACK, Blocks.BLACKSTONE);

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        CompoundTag tag = stack.getTag();
        String direction = "horizontal";
        int level = 2;
        int xp = 0;
        if (tag != null) {
            direction = tag.getBoolean("horizontal") ? "horizontal" : "vertical";
            level = tag.getInt("level");
            xp = tag.getInt("xp");
        }
        tooltip.add(new TranslatableText("tooltip.cpp.cfom.direction", new TranslatableText("tooltip.cpp.cfom." + direction)).formatted(Formatting.GREEN));
        tooltip.add(new TranslatableText("misc.cpp", new TranslatableText("tooltip.cpp.cfom.level", level), new TranslatableText("tooltip.cpp.cfom.xp", xp)).formatted(Formatting.GREEN));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack item = user.getStackInHand(hand);
        if (!world.isClient) {
            CompoundTag tag = item.getTag();
            assert tag != null; // 没用, 只为去除警告
            BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
            BlockPos blockPos = hitResult.getBlockPos();
            if (user.isSneaking() && hitResult.getType() == HitResult.Type.MISS) {
                tag.putBoolean("horizontal", !tag.getBoolean("horizontal"));
                item.setTag(tag);
                ((ServerPlayerEntity)user).networkHandler.sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.ACTIONBAR,
                        new TranslatableText("misc.cpp",
                                new TranslatableText("tooltip.cpp.cfom.direction",
                                        new TranslatableText("tooltip.cpp.cfom." + (tag.getBoolean("horizontal") ? "horizontal" : "vertical"))),
                                new TranslatableText("misc.cpp",
                                        new TranslatableText("tooltip.cpp.cfom.level", tag.getInt("level")),
                                        new TranslatableText("tooltip.cpp.cfom.xp", tag.getInt("xp")))
                        ).formatted(Formatting.GREEN)
                ));
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(item);
            }else if (!user.isSneaking() && hitResult.getType() == HitResult.Type.BLOCK) {
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                if (user.isCreative()) {
                    if (fill(world, user, blockPos, item, tag))
                        return TypedActionResult.success(user.getStackInHand(hand));
                }else if (getExperience(user) >= 4) {
                    if (fill(world, user, blockPos, item, tag)) {
                        user.addExperience(-4);
                        return TypedActionResult.success(user.getStackInHand(hand));
                    }
                }else {
                    say(user, new TranslatableText("chat.cpp.exp.less"));
                }
            }
        }
        return TypedActionResult.pass(item);
    }

    private static Boolean fill(World world, PlayerEntity user, BlockPos blockPos, ItemStack item, CompoundTag tag) {
        int level = tag.getInt("level"), xp = tag.getInt("xp");
        int length = 32, high = level;
        if (!tag.getBoolean("horizontal")) {
            length = level;
            high = 32;
        }
        int value = 0;
        for (int i = 0; i < high; i++) {
            BlockPos pos = blockPos.add(0, i-1, 0);
            for (int j = 0; j < length; j++) {
                if (canClear.contains(world.getBlockState(pos).getBlock())) {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    value++;
                }
                pos = pos.add(user.getHorizontalFacing().getVector());
            }
        }
        if (value > 0) {
            if (++xp >= level * level) {
                level = ++level > 32 ? 32 : level;
                xp = 0;
            }
            tag.putInt("level", level);
            tag.putInt("xp", xp);
            item.setTag(tag);
            return true;
        }
        return false;
    }
}
