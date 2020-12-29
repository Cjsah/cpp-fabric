package net.cpp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import static net.cpp.api.CppChat.say;

public class TimeConditioner extends Item {

    private static byte mode = 0;

    public TimeConditioner(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack item = user.getStackInHand(hand);
        if (!world.isClient) {
            switch (mode) {
                case 0: {
                    //random 6
                    world.getGameRules().get(GameRules.RANDOM_TICK_SPEED);
                    say(user, new TranslatableText("misc.cpp", user.getName(), new TranslatableText("chat.cpp.time.accelerate")));
                    break;
                }
                case 1: {
                    //random 3
                    world.getGameRules().get(GameRules.RANDOM_TICK_SPEED);
                    say(user, new TranslatableText("misc.cpp", user.getName(), new TranslatableText("chat.cpp.time.normal")));
                    break;
                }
                case 2: {
                    //time false
                    world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, world.getServer());
                    say(user, new TranslatableText("misc.cpp", user.getName(), new TranslatableText("chat.cpp.time.uncycle")));
                    break;
                }
                case 3: {
                    //time true
                    world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(true, world.getServer());
                    say(user, new TranslatableText("misc.cpp", user.getName(), new TranslatableText("chat.cpp.time.cycle")));
                    break;
                }
            }
            mode++;
            if (mode >= 4) mode = 0;
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.success(item);
        }
        return TypedActionResult.pass(item);
    }
}
