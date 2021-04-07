package net.cpp.rituals;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import javax.annotation.Nullable;

public class RitualResult {

    /**
     * @param rare1 稀有掉落物1
     * @param rare2 稀有掉落物2
     * @return 随机附魔书
     */
    protected static ItemStack getEnchantingResult(Item rare1, Item rare2) {
        return null;
    }

    /**
     * @param agentia 使用的药剂
     * @param item 被附加效果的装备
     * @return 附加效果后的装备
     */
    protected static ItemStack getEffectResult(Item agentia, ItemStack item) {
        return null;
    }

    /**
     * @param item 被附加属性的装备
     * @return 附加属性后的装备
     */
    protected static ItemStack getAttributeResult(ItemStack item) {
        return null;
    }

    /**
     * @return 刷怪笼
     */
    protected static ItemStack getDarkResult() {
        return new ItemStack(Items.SPAWNER);
    }

    /**
     * @return 紫水晶母岩
     */
    protected static ItemStack getLightResult() {
        return new ItemStack(Items.AMETHYST_BLOCK);
    }

    public interface IRitualResult {
        ItemStack get(ItemStack input, @Nullable Item... items);
    }
}
