package cpp.rituals;

import cpp.Craftingpp;
import cpp.ducktyping.ICppState;
import cpp.init.CppItemTags;
import cpp.init.CppItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public final class RitualResult {

    /**
     * @param rare1 稀有掉落物1
     * @param rare2 稀有掉落物2
     * @return 随机附魔书
     */
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public static ItemStack getEnchantingResult(Item rare1, Item rare2) {
        List<Item> rares = CppItemTags.RARE_DROPS.values();
        int index = rares.indexOf(rare1) * rares.size() + rares.indexOf(rare2);
        String enchantId = ((ICppState)MinecraftClient.getInstance().getServer()).getCppStateOperate().getEnchantment(index);
        Enchantment enchantment = Registry.ENCHANTMENT.get(new Identifier(enchantId));
        ItemStack enchantedBook = Items.ENCHANTED_BOOK.getDefaultStack();
        EnchantedBookItem.addEnchantment(enchantedBook, new EnchantmentLevelEntry(enchantment, enchantment.getMaxLevel()));
        return enchantedBook;
    }

    /**
     * @param agentia 使用的药剂
     * @param item 被附加效果的装备
     * @return 附加效果后的装备
     */
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public static ItemStack getEffectResult(@Nonnull Item agentia, ItemStack item) {
        FoodComponent foodComponent = agentia.getFoodComponent();
        String tag;
        if (foodComponent != null) {
            StatusEffect effect = foodComponent.getStatusEffects().get(0).getFirst().getEffectType();
            tag = Registry.STATUS_EFFECT.getId(effect).toString();
        }else if (agentia == CppItems.MAGNET) {
            tag = Craftingpp.MOD_ID3 + "magnet";
        }else {
            tag = "";
        }
        item.putSubTag("EquipmentEffect", StringTag.of(tag));

        return null;
    }

    /**
     * @param item 被附加属性的装备
     * @return 附加属性后的装备
     */
    @Nonnull
    public static ItemStack getAttributeResult(ItemStack item) {
        return null;
    }

    /**
     * @return 刷怪笼
     */
    @Nonnull
    public static ItemStack getDarkResult() {
        return new ItemStack(Items.SPAWNER);
    }

    /**
     * @return 紫水晶母岩
     */
    @Nonnull
    public static ItemStack getLightResult() {
        return new ItemStack(Items.AMETHYST_BLOCK);
    }

    public interface IRitualResult {
        ItemStack get(ItemStack input, @Nullable Item... items);
    }

//    class EffectRecipes {
//        private final Item agentia;
//        private final StatusEffect effect;
//        private final EquipmentSlot slot;
//
//        private EffectRecipes(Item agentia, StatusEffect) {
//            EquipmentSlot.MAINHAND;
//        }
//
//    }
}
