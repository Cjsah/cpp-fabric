package net.cpp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import static net.cpp.api.Chat.say;

public class BlueForceOfSky extends Item {

    public BlueForceOfSky(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            boolean raining = world.getLevelProperties().isRaining();
            boolean thundering = world.getLevelProperties().isThundering();

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
        return super.use(world, user, hand);
    }
}
