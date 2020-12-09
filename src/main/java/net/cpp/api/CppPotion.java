package net.cpp.api;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CppPotion extends PotionItem {
    public final ImmutableList<StatusEffectInstance> effect;

    public CppPotion(Settings settings) {
        super(settings);
        this.effect = null;
    }

    public CppPotion(Settings settings, StatusEffectInstance... effect) {
        super(settings);
        this.effect = ImmutableList.copyOf(effect);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (this.isFood()) {
            user.eatFood(world, stack);
        }
        return super.finishUsing(stack, world, user);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey();
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            if (this.effect != null) {
                stacks.add(PotionUtil.setCustomPotionEffects(new ItemStack(this), this.effect));
            }else {
                stacks.add(PotionUtil.setPotion(new ItemStack(this), Potions.EMPTY));
            }
        }
    }

}
