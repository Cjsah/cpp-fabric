package cpp.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cpp.api.ICppConfig;
import cpp.api.IDefaultNbtItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static cpp.api.CppChat.say;

public class CyanForceOfMountain extends Item implements IDefaultNbtItem, ICppConfig {

    private static JsonObject config;

    public CyanForceOfMountain(Settings settings) {
        super(settings);
        config = this.getConfig();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Text getName(ItemStack stack) {
        return new TranslatableText(this.getTranslationKey()).formatted(Formatting.GOLD);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateTag();
        tooltip.add(new TranslatableText("tooltip.cpp.cfom.direction", new TranslatableText("tooltip.cpp.cfom." + (nbt.getBoolean("horizontal") ? "horizontal" : "vertical"))).formatted(Formatting.GREEN));
        tooltip.add(new TranslatableText("misc.cpp", new TranslatableText("tooltip.cpp.cfom.level", nbt.getInt("level")), new TranslatableText("tooltip.cpp.cfom.xp", nbt.getInt("xp"))).formatted(Formatting.GREEN));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack item = user.getStackInHand(hand);
        if (!world.isClient) {
            NbtCompound nbt = item.getOrCreateTag();
            BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
            BlockPos blockPos = hitResult.getBlockPos();
            if (user.isSneaking() && hitResult.getType() == HitResult.Type.MISS) {
                nbt.putBoolean("horizontal", !nbt.getBoolean("horizontal"));
                ((ServerPlayerEntity)user).networkHandler.sendPacket(new TitleS2CPacket(//FIXME
                        new TranslatableText("misc.cpp",
                                new TranslatableText("tooltip.cpp.cfom.direction",
                                        new TranslatableText("tooltip.cpp.cfom." + (nbt.getBoolean("horizontal") ? "horizontal" : "vertical"))),
                                new TranslatableText("misc.cpp",
                                        new TranslatableText("tooltip.cpp.cfom.level", nbt.getInt("level")),
                                        new TranslatableText("tooltip.cpp.cfom.xp", nbt.getInt("xp")))
                        ).formatted(Formatting.GREEN)
                ));
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(item);
            }else if (!user.isSneaking() && hitResult.getType() == HitResult.Type.BLOCK) {
                if (user.isCreative()) {
                    if (fill(world, user, blockPos, nbt))
                        user.incrementStat(Stats.USED.getOrCreateStat(this));
                    return TypedActionResult.success(user.getStackInHand(hand));
                }else if (user.totalExperience >= 4) {
                    if (fill(world, user, blockPos, nbt)) {
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

    private static Boolean fill(World world, PlayerEntity user, BlockPos blockPos, NbtCompound nbt) {
        int level = nbt.getInt("level"), xp = nbt.getInt("xp");
        int length = 32, high = level;
        if (!nbt.getBoolean("horizontal")) {
            length = level;
            high = 32;
        }
        int value = 0;
        ArrayList<Block> canBreak = new ArrayList<>();
        for (JsonElement name : config.get("CanBreak").getAsJsonArray()) {
            canBreak.add(Registry.BLOCK.get(new Identifier(name.getAsString())));

        }
            for (int i = 0; i < high; i++) {
            BlockPos pos = blockPos.add(0, i-1, 0);
            for (int j = 0; j < length; j++) {

                if (canBreak.contains(world.getBlockState(pos).getBlock())) {
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
            nbt.putInt("level", level);
            nbt.putInt("xp", xp);
            return true;
        }
        return false;
    }

    public NbtCompound getDefaultNbt(NbtCompound nbt) {
        nbt.putBoolean("horizontal", true);
        nbt.putInt("level", config.get("StartLevel").getAsInt());
        nbt.putInt("xp", 0);
        return nbt;
	}

    @Override
    public String getConfigName() {
        return "cyan_force_of_mountain";
    }
}
