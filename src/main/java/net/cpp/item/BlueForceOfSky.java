package net.cpp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import static net.cpp.api.CppChat.say;

public class BlueForceOfSky extends Item {

    public BlueForceOfSky(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            if (user.isCreative()) {
                changeWeather(world, user, hand);
                return TypedActionResult.success(user.getStackInHand(hand));
            }else if (user.experienceLevel >= 1) {
                changeWeather(world, user, hand);
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(user.getStackInHand(hand));
            }
            say(user, new TranslatableText("chat.cpp.exp.less"));
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    private static void changeWeather(World world, PlayerEntity user, Hand hand) {
        boolean raining = world.getLevelProperties().isRaining();
        boolean thundering = world.getLevelProperties().isThundering();
        user.addExperience(-9);
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
