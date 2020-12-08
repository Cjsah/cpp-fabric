package net.cpp.api;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.collection.DefaultedList;
import java.util.ArrayList;

public class CppPotion extends PotionItem {
    public final ImmutableList<StatusEffectInstance> effect;

    public CppPotion(Settings settings) {
        super(settings);
        this.effect = ImmutableList.copyOf(new ArrayList<>());
    }

    public CppPotion(Settings settings, StatusEffectInstance... effect) {
        super(settings);
        this.effect = ImmutableList.copyOf(effect);
        System.out.println(super.isFood());
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey();
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            if (!this.effect.isEmpty()) {
                stacks.add(PotionUtil.setCustomPotionEffects(new ItemStack(this), this.effect));
            }
        }
    }

}
