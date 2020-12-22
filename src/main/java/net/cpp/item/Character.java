package net.cpp.item;

import net.cpp.init.CppItems;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

import java.util.Random;

public class Character extends Item {
    public Character(Settings settings) {
        super(settings);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            for (int i = 1; i <= 117; i++) {
                CompoundTag tag = new CompoundTag();
                ItemStack item = new ItemStack(this);
                tag.putInt("character", i);
                item.setTag(tag);
                stacks.add(item);
            }
        }
    }

    public static ItemStack randomGetOne() {
        ItemStack item = new ItemStack(CppItems.CHARACTER);
        CompoundTag tag = new CompoundTag();
        tag.putInt("character", new Random().nextInt(117)+1);
        item.setTag(tag);
        return item;
    }
}
