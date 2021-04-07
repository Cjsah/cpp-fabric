package net.cpp.rituals;

import net.cpp.init.CppItemTags;
import net.cpp.init.CppItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Collections;
import java.util.List;
@SuppressWarnings("ConstantConditions")
public enum RitualRecipes {
    ENCHANTING_BOOK(
            Ritual.RitualType.MAGIC,
            (input, items) -> {return RitualResult.getEnchantingResult(items[0], items[1]);},
            list(Items.EXPERIENCE_BOTTLE), CppItemTags.RARE_DROPS.values(), list(Items.EXPERIENCE_BOTTLE),
            list(Items.LAPIS_LAZULI), list(Items.LAPIS_LAZULI), list(Items.LAPIS_LAZULI),
            list(Items.EXPERIENCE_BOTTLE), CppItemTags.RARE_DROPS.values(), list(Items.EXPERIENCE_BOTTLE)
    ),
    EFFECT(
            Ritual.RitualType.MAGIC,
            (input, items) -> {return RitualResult.getEffectResult(items[0], input);},
            CppItemTags.AGENTIAS.values(), list(Items.EXPERIENCE_BOTTLE), CppItemTags.AGENTIAS.values(),
            list(Items.EXPERIENCE_BOTTLE), CppItemTags.RARE_DROPS.values(), list(Items.EXPERIENCE_BOTTLE),
            CppItemTags.AGENTIAS.values(), list(Items.EXPERIENCE_BOTTLE), CppItemTags.AGENTIAS.values()
    ),
    ATTRIBUTE(
            Ritual.RitualType.MAGIC,
            (input, items) -> {return RitualResult.getAttributeResult(input);},
            list(Items.GOLD_INGOT), list(Items.EXPERIENCE_BOTTLE), list(Items.GOLD_INGOT),
            list(Items.EXPERIENCE_BOTTLE), CppItemTags.RARE_DROPS.values(), list(Items.EXPERIENCE_BOTTLE),
            list(Items.GOLD_INGOT), list(Items.EXPERIENCE_BOTTLE), list(Items.GOLD_INGOT)
    ),
    DARK(
            Ritual.RitualType.ANCIENT,
            (input, items) -> {return RitualResult.getDarkResult();},
            list(CppItems.SHARD_OF_THE_DARKNESS), list(CppItems.SHARD_OF_THE_DARKNESS), list(CppItems.SHARD_OF_THE_DARKNESS),
            list(CppItems.SHARD_OF_THE_DARKNESS), CppItemTags.RARE_DROPS.values(), list(CppItems.SHARD_OF_THE_DARKNESS),
            list(CppItems.SHARD_OF_THE_DARKNESS), list(CppItems.SHARD_OF_THE_DARKNESS), list(CppItems.SHARD_OF_THE_DARKNESS)
    ),
    LIGHT(
            Ritual.RitualType.ANCIENT,
            (input, items) -> {return RitualResult.getLightResult();},
            list(Items.AMETHYST_SHARD), list(Items.AMETHYST_SHARD), list(Items.AMETHYST_SHARD),
            list(Items.AMETHYST_SHARD), list(CppItems.HEART_OF_CRYSTAL), list(Items.AMETHYST_SHARD),
            list(Items.AMETHYST_SHARD), list(Items.AMETHYST_SHARD), list(Items.AMETHYST_SHARD)
    );

    private final Ritual.RitualType type;
    private final RitualResult.IRitualResult result;
    private final List<Item>[] items;

    @SafeVarargs
    RitualRecipes(Ritual.RitualType type, RitualResult.IRitualResult result, List<Item>... items) {
        this.type = type;
        this.result = result;
        this.items = items;
    }

    public Ritual.RitualType getType() {
        return type;
    }

    public ItemStack getResult(ItemStack input, Item... items) {
        return result.get(input, items);
    }

    public List<Item>[] getItems() {
        return items;
    }

    private static List<Item> list(Item item) {
        return Collections.singletonList(item);
    }

}
