package net.cpp.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import static net.cpp.api.CppChat.say;

public class BlueForceOfSky extends Item {

    public BlueForceOfSky(Settings settings) {
        super(settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Text getName(ItemStack stack) {
        return new TranslatableText(this.getTranslationKey()).formatted(Formatting.GOLD);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            if (user.isCreative()) {
                changeWeather(world, user, this);
                return TypedActionResult.success(user.getStackInHand(hand));
            }else if (user.experienceLevel >= 1) {
                changeWeather(world, user, this);
                user.addExperience(-9);
                return TypedActionResult.success(user.getStackInHand(hand));
            }
            say(user, new TranslatableText("chat.cpp.exp.less"));
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    private static void changeWeather(World world, PlayerEntity user, BlueForceOfSky item) {
        boolean raining = world.getLevelProperties().isRaining();
        boolean thundering = world.getLevelProperties().isThundering();
        user.incrementStat(Stats.USED.getOrCreateStat(item));
        if (thundering) {
            ((ServerWorld)world).setWeather(6000, 0, false, false);
            say(user, new TranslatableText("commands.weather.set.clear"));
        }else if (raining) {
            ((ServerWorld)world).setWeather(0, 6000, true, true);
            say(user, new TranslatableText("commands.weather.set.thunder"));
        }else {
            ((ServerWorld)world).setWeather(0, 6000, true, false);
            say(user, new TranslatableText("commands.weather.set.rain"));
        }
    }
}
