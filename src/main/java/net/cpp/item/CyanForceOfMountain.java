package net.cpp.item;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import net.cpp.api.ICppConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static net.cpp.api.CppChat.say;
import static net.cpp.api.CodingTool.getExperience;

public class CyanForceOfMountain extends Item implements IDefaultTagItem, ICppConfig {

    private static JsonObject config;

    public CyanForceOfMountain(Settings settings) {
        super(settings);
        config = this.getConfig();
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
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        CompoundTag tag = stack.getOrCreateTag();
        tooltip.add(new TranslatableText("tooltip.cpp.cfom.direction", new TranslatableText("tooltip.cpp.cfom." + (tag.getBoolean("horizontal") ? "horizontal" : "vertical"))).formatted(Formatting.GREEN));
        tooltip.add(new TranslatableText("misc.cpp", new TranslatableText("tooltip.cpp.cfom.level", tag.getInt("level")), new TranslatableText("tooltip.cpp.cfom.xp", tag.getInt("xp"))).formatted(Formatting.GREEN));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack item = user.getStackInHand(hand);
        if (!world.isClient) {
            CompoundTag tag = item.getOrCreateTag();
            BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
            BlockPos blockPos = hitResult.getBlockPos();
            if (user.isSneaking() && hitResult.getType() == HitResult.Type.MISS) {
                tag.putBoolean("horizontal", !tag.getBoolean("horizontal"));
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
                if (user.isCreative()) {
                    if (fill(world, user, blockPos, tag))
                        user.incrementStat(Stats.USED.getOrCreateStat(this));
                    return TypedActionResult.success(user.getStackInHand(hand));
                }else if (getExperience(user) >= 4) {
                    if (fill(world, user, blockPos, tag)) {
                        user.addExperience(-4);
                        user.incrementStat(Stats.USED.getOrCreateStat(this));
                        return TypedActionResult.success(user.getStackInHand(hand));
                    }
                }else {
                    say(user, new TranslatableText("chat.cpp.exp.less"));
                }
            }
        }
        return TypedActionResult.pass(item);
    }

    private static Boolean fill(World world, PlayerEntity user, BlockPos blockPos, CompoundTag tag) {
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
            return true;
        }
        return false;
    }

    public CompoundTag modifyDefaultTag(CompoundTag tag) {
		tag.putBoolean("horizontal", true);
        tag.putInt("level", config.get("StartLevel").getAsInt());
        tag.putInt("xp", 0);
        return tag;
	}

    @Override
    public String getConfigName() {
        return "cyan_force_of_mountains";
    }

    @Override
    public JsonObject defaultConfig(JsonObject json) {
        json.addProperty("StartLevel", 2);
        return json;
    }
}
