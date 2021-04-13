package cpp.rituals;

import cpp.ducktyping.ICppState;
import cpp.init.CppItemTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(enchantedBook, new EnchantmentLevelEntry(enchantment, enchantment.getMaxLevel()));
        return enchantedBook;
    }

    /**
     * @param agentia 使用的药剂
     * @param item 被附加效果的装备
     * @return 附加效果后的装备
     */
    @Nonnull
    protected static ItemStack getEffectResult(Item agentia, ItemStack item) {
        return null;
    }

    /**
     * @param item 被附加属性的装备
     * @return 附加属性后的装备
     */
    @Nonnull
    protected static ItemStack getAttributeResult(ItemStack item) {
        return null;
    }

    /**
     * @return 刷怪笼
     */
    @Nonnull
    protected static ItemStack getDarkResult() {
        return new ItemStack(Items.SPAWNER);
    }

    /**
     * @return 紫水晶母岩
     */
    @Nonnull
    protected static ItemStack getLightResult() {
        return new ItemStack(Items.AMETHYST_BLOCK);
    }

    public interface IRitualResult {
        ItemStack get(ItemStack input, @Nullable Item... items);
    }
}
