package net.cpp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import static net.cpp.api.CodingTool.getExperience;

public class TimeChecker extends Item {
    public TimeChecker(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack item = user.getStackInHand(hand);
        if (!world.isClient) {
            if (user.isCreative()) {
                ((ServerWorld)world).setTimeOfDay(world.getTimeOfDay() + 1200L);
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(item);
            }else if (getExperience(user) >= 4) {
                user.addExperience(-4);
                ((ServerWorld)world).setTimeOfDay(world.getTimeOfDay() + 1200L);
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(item);
            }
        }
        return TypedActionResult.pass(item);
    }

}
