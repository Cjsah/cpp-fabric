package net.cpp.item;

import com.google.common.collect.ImmutableList;
import net.cpp.init.CppItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class AncientScroll extends Item {

    private static final ImmutableList<String> list = ImmutableList.of("fortune", "looting", "respiration", "lure", "unbreaking", "sweeping", "power", "sharpness");

    public AncientScroll(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasTag()) {
            assert stack.getTag() != null; // 没用, 只为去除警告
            tooltip.add(new TranslatableText("enchantment.minecraft."+ stack.getTag().getString("enchantment")).formatted(Formatting.DARK_PURPLE));
        }
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            for (String i : list) {
                stacks.add(newItem(i));
            }
        }
    }

    private ItemStack newItem(String name) {
        CompoundTag tag = new CompoundTag();
        ItemStack item = new ItemStack(this);
        tag.putString("enchantment", name);
        item.setTag(tag);
        return item;
    }

    public static ItemStack randomGetOne() {
        ItemStack item = new ItemStack(CppItems.ANCIENT_SCROLL);
        CompoundTag tag = new CompoundTag();
        tag.putString("enchantment", list.get(new Random().nextInt(list.size())));
        item.setTag(tag);
        return item;
    }

    public static ItemStack getter(String name) {
        if (list.contains(name)) {
            ItemStack item = new ItemStack(CppItems.ANCIENT_SCROLL);
            CompoundTag tag = new CompoundTag();
            tag.putString("enchantment", name);
            item.setTag(tag);
            return item;
        }else return null;
    }


}
