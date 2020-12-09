package net.cpp.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CppFood extends Item {
    public final ImmutableList<StatusEffectInstance> effect;

    public CppFood(Settings settings) {
        super(settings);
        this.effect = null;
    }

    public CppFood(Settings settings, StatusEffectInstance... effect) {
        super(settings);
        this.effect = ImmutableList.copyOf(effect);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        List<StatusEffectInstance> list2 = getFoodEffects(stack);
        List<Pair<EntityAttribute, EntityAttributeModifier>> list3 = Lists.newArrayList();
        Iterator var5;
        TranslatableText mutableText;
        StatusEffect statusEffect;
        if (!list2.isEmpty()) {
            for(var5 = list2.iterator(); var5.hasNext(); tooltip.add(mutableText.formatted(statusEffect.getType().getFormatting()))) {
                StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var5.next();
                mutableText = new TranslatableText(statusEffectInstance.getTranslationKey());
                statusEffect = statusEffectInstance.getEffectType();
                Map<EntityAttribute, EntityAttributeModifier> map = statusEffect.getAttributeModifiers();
                if (!map.isEmpty()) {

                    for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : map.entrySet()) {
                        EntityAttributeModifier entityAttributeModifier = entry.getValue();
                        EntityAttributeModifier entityAttributeModifier2 = new EntityAttributeModifier(entityAttributeModifier.getName(), statusEffect.adjustModifierAmount(statusEffectInstance.getAmplifier(), entityAttributeModifier), entityAttributeModifier.getOperation());
                        list3.add(new Pair(entry.getKey(), entityAttributeModifier2));
                    }
                }

                if (statusEffectInstance.getAmplifier() > 0) {
                    mutableText = new TranslatableText("potion.withAmplifier", mutableText, new TranslatableText("potion.potency." + statusEffectInstance.getAmplifier()));
                }

                if (statusEffectInstance.getDuration() > 20) {
                    mutableText = new TranslatableText("potion.withDuration", mutableText, StatusEffectUtil.durationToString(statusEffectInstance, 1.0F));
                }
            }
        }

        if (!list3.isEmpty()) {
            tooltip.add(LiteralText.EMPTY);
            tooltip.add((new TranslatableText("potion.whenDrank")).formatted(Formatting.DARK_PURPLE));

            for (Pair<EntityAttribute, EntityAttributeModifier> pair : list3) {
                EntityAttributeModifier entityAttributeModifier3 = pair.getSecond();
                double d = entityAttributeModifier3.getValue();
                double g;
                if (entityAttributeModifier3.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_BASE && entityAttributeModifier3.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
                    g = entityAttributeModifier3.getValue();
                } else {
                    g = entityAttributeModifier3.getValue() * 100.0D;
                }

                if (d > 0.0D) {
                    tooltip.add((new TranslatableText("attribute.modifier.plus." + entityAttributeModifier3.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(g), new TranslatableText(pair.getFirst().getTranslationKey()))).formatted(Formatting.BLUE));
                } else if (d < 0.0D) {
                    g *= -1.0D;
                    tooltip.add((new TranslatableText("attribute.modifier.take." + entityAttributeModifier3.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(g), new TranslatableText(pair.getFirst().getTranslationKey()))).formatted(Formatting.RED));
                }

            }
        }
    }

    public static List<StatusEffectInstance> getFoodEffects(ItemStack stack) {
        List<StatusEffectInstance> list = Lists.newArrayList();
        List<Pair<StatusEffectInstance, Float>> effectList = Objects.requireNonNull(stack.getItem().getFoodComponent()).getStatusEffects();

        for (Pair<StatusEffectInstance, Float> effect : effectList) {
            list.add(effect.getFirst());
        }
        return list;
    }
}
