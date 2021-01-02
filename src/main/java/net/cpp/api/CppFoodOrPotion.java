package net.cpp.api;

import java.util.*;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CppFoodOrPotion extends Item implements INutrition {
    public static final Map<Item, Integer> map = new HashMap<>();
    private static final MutableText field_25817;
    private final UseAction useAction;
    private final int nutrition;

    public CppFoodOrPotion(UseAction useAction, int nutrition, Settings settings) {
        super(settings);
        this.useAction = useAction;
        this.nutrition = nutrition;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return this.useAction;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        PlayerEntity playerEntity = (PlayerEntity)user;

        if (this.isFood()) {
            stack = this.eatFood(world, stack, playerEntity);
        }

        if (this.useAction == UseAction.DRINK && (playerEntity == null || !playerEntity.isCreative())) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (playerEntity != null) {
                playerEntity.giveItemStack(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return stack;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        List<StatusEffectInstance> list2 = getFoodEffects(stack);
        List<Pair<EntityAttribute, EntityAttributeModifier>> list3 = Lists.newArrayList();
        Iterator<StatusEffectInstance> var5;
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
                        list3.add(new Pair<EntityAttribute, EntityAttributeModifier>(entry.getKey(), entityAttributeModifier2));
                    }
                }

                if (statusEffectInstance.getAmplifier() > 0) {
                    mutableText = new TranslatableText("potion.withAmplifier", mutableText, new TranslatableText("potion.potency." + statusEffectInstance.getAmplifier()));
                }

                if (statusEffectInstance.getDuration() > 20) {
                    mutableText = new TranslatableText("potion.withDuration", mutableText, StatusEffectUtil.durationToString(statusEffectInstance, 1.0F));
                }
            }
        }else if (this.useAction == UseAction.DRINK) {
            tooltip.add(field_25817);
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

    public ItemStack eatFood(World world, ItemStack stack, PlayerEntity player) {
        if (this.useAction == UseAction.EAT) {
            return player.eatFood(world, stack);
        }
        player.getHungerManager().eat(stack.getItem(), stack);
        player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        if (player instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)player, stack);
        }
        applyFoodEffects(stack.getItem(), world, player);
        if (!player.isCreative()) {
            stack.decrement(1);
        }
        return stack;
    }

    private void applyFoodEffects(Item item, World world, LivingEntity targetEntity) {
        List<Pair<StatusEffectInstance, Float>> list = Objects.requireNonNull(item.getFoodComponent()).getStatusEffects();

        for (Pair<StatusEffectInstance, Float> pair : list) {
            if (!world.isClient && pair.getFirst() != null && world.random.nextFloat() < pair.getSecond()) {
                targetEntity.addStatusEffect(new StatusEffectInstance(pair.getFirst()));
            }
        }
    }

    @Override
    public int getNutrition(ItemStack itemStack) {
        return this.nutrition;
    }

    static {
        field_25817 = (new TranslatableText("effect.none")).formatted(Formatting.GRAY);

        map.put(Items.MUSHROOM_STEM, -6);
        map.put(Items.BEETROOT_SOUP, -6);
        map.put(Items.SUSPICIOUS_STEW, -6);
        map.put(Items.BREAD, -5);
        map.put(Items.BAKED_POTATO, -5);
        map.put(Items.APPLE, -4);
        map.put(Items.GOLDEN_APPLE, -4);
        map.put(Items.ENCHANTED_GOLDEN_APPLE, -4);
        map.put(Items.CHORUS_FRUIT, -4);
        map.put(Items.PUMPKIN_PIE, -3);
        map.put(Items.CARROT, -3);
        map.put(Items.GOLDEN_CARROT, -3);
        map.put(Items.MELON_SLICE, -2);
        map.put(Items.COOKIE, -2);
        map.put(Items.POISONOUS_POTATO, -2);
        map.put(Items.SWEET_BERRIES, -2);
        map.put(Items.DRIED_KELP, -1);
        map.put(Items.BEETROOT, -1);
        map.put(Items.POTATO, -1);
        map.put(Items.TROPICAL_FISH, 1);
        map.put(Items.PUFFERFISH, 1);
        map.put(Items.SPIDER_EYE, 2);
        map.put(Items.MUTTON, 2);
        map.put(Items.RABBIT, 2);
        map.put(Items.CHICKEN, 2);
        map.put(Items.COD, 2);
        map.put(Items.SALMON, 2);
        map.put(Items.BEEF, 3);
        map.put(Items.PORKCHOP, 3);
        map.put(Items.HONEY_BOTTLE, 3);
        map.put(Items.ROTTEN_FLESH, 4);
        map.put(Items.COOKED_RABBIT, 5);
        map.put(Items.COOKED_COD, 5);
        map.put(Items.COOKED_CHICKEN, 6);
        map.put(Items.COOKED_MUTTON, 6);
        map.put(Items.COOKED_SALMON, 6);
        map.put(Items.COOKED_BEEF, 8);
        map.put(Items.COOKED_PORKCHOP, 8);
    }
}
