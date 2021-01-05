package net.cpp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Temperancer extends Item implements IDefaultTagItem {
    public Temperancer(Settings settings) {
        super(settings);
    }

    @Override
    public CompoundTag modifyDefaultTag(CompoundTag tag) {
        tag.putBoolean("open", false);
        return tag;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack) + (stack.getOrCreateTag().getBoolean("open") ? ".on" : ".off");
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack item = user.getStackInHand(hand);
        if (!world.isClient) {
            CompoundTag tag = item.getOrCreateTag();
            tag.putBoolean("open", !tag.getBoolean("open"));
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.success(item);
        }
        return TypedActionResult.pass(item);
    }
}
