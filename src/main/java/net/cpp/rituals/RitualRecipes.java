package net.cpp.rituals;

import net.cpp.init.CppItemTags;
import net.cpp.init.CppItems;
import net.cpp.rituals.result.ARitualResult;
import net.cpp.rituals.result.AttributeResult;
import net.cpp.rituals.result.DarkResult;
import net.cpp.rituals.result.EffectResult;
import net.cpp.rituals.result.EnchantingResult;
import net.cpp.rituals.result.LightResult;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Collections;
import java.util.List;

public enum RitualRecipes {
    ENCHANTING_BOOK(
            Ritual.RitualType.MAGIC,
            new EnchantingResult(),
            list(Items.EXPERIENCE_BOTTLE), CppItemTags.RARE_DROPS.values(), list(Items.EXPERIENCE_BOTTLE),
            list(Items.LAPIS_LAZULI), list(Items.LAPIS_LAZULI), list(Items.LAPIS_LAZULI),
            list(Items.EXPERIENCE_BOTTLE), CppItemTags.RARE_DROPS.values(), list(Items.EXPERIENCE_BOTTLE)
    ),
    EFFECT(
            Ritual.RitualType.MAGIC,
            new EffectResult(),
            CppItemTags.AGENTIAS.values(), list(Items.EXPERIENCE_BOTTLE), CppItemTags.AGENTIAS.values(),
            list(Items.EXPERIENCE_BOTTLE), CppItemTags.RARE_DROPS.values(), list(Items.EXPERIENCE_BOTTLE),
            CppItemTags.AGENTIAS.values(), list(Items.EXPERIENCE_BOTTLE), CppItemTags.AGENTIAS.values()
    ),
    ATTRIBUTE(
            Ritual.RitualType.MAGIC,
            new AttributeResult(),
            list(Items.GOLD_INGOT), list(Items.EXPERIENCE_BOTTLE), list(Items.GOLD_INGOT),
            list(Items.EXPERIENCE_BOTTLE), CppItemTags.RARE_DROPS.values(), list(Items.EXPERIENCE_BOTTLE),
            list(Items.GOLD_INGOT), list(Items.EXPERIENCE_BOTTLE), list(Items.GOLD_INGOT)
    ),
    DARK(
            Ritual.RitualType.ANCIENT,
            new DarkResult(),
            list(CppItems.SHARD_OF_THE_DARKNESS), list(CppItems.SHARD_OF_THE_DARKNESS), list(CppItems.SHARD_OF_THE_DARKNESS),
            list(CppItems.SHARD_OF_THE_DARKNESS), CppItemTags.RARE_DROPS.values(), list(CppItems.SHARD_OF_THE_DARKNESS),
            list(CppItems.SHARD_OF_THE_DARKNESS), list(CppItems.SHARD_OF_THE_DARKNESS), list(CppItems.SHARD_OF_THE_DARKNESS)
    ),
    LIGHT(
            Ritual.RitualType.ANCIENT,
            new LightResult(),
            list(Items.AMETHYST_SHARD), list(Items.AMETHYST_SHARD), list(Items.AMETHYST_SHARD),
            list(Items.AMETHYST_SHARD), list(CppItems.HEART_OF_CRYSTAL), list(Items.AMETHYST_SHARD),
            list(Items.AMETHYST_SHARD), list(Items.AMETHYST_SHARD), list(Items.AMETHYST_SHARD)
    );

    private final Ritual.RitualType type;
    private final ARitualResult result;
    private final List<Item>[] items;

    @SafeVarargs
    RitualRecipes(Ritual.RitualType type, ARitualResult result, List<Item>... items) {
        this.type = type;
        this.result = result;
        this.items = items;
    }

    public Ritual.RitualType getType() {
        return type;
    }

    public ItemStack getResult() {
        return result.get();
    }

    public List<Item>[] getItems() {
        return items;
    }

    private static List<Item> list(Item item) {
        return Collections.singletonList(item);
    }

}
