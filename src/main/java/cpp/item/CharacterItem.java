package cpp.item;

import cpp.init.CppItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.collection.DefaultedList;

import java.util.Random;

public class CharacterItem extends Item {
    public CharacterItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            for (int i = 1; i <= 117; i++) {
                NbtCompound nbt = new NbtCompound();
                ItemStack item = new ItemStack(this);
                nbt.putInt("character", i);
                item.setTag(nbt);
                stacks.add(item);
            }
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        // 还不能贴在床上
        return ActionResult.PASS;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack) + (stack.hasTag() ? stack.getTag().getInt("character") : "");
    }

    public static ItemStack randomGetOne() {
        ItemStack item = new ItemStack(CppItems.CHARACTER);
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("character", new Random().nextInt(117)+1);
        item.setTag(nbt);
        return item;
    }

    public static ItemStack get(int id) {
        if (id >= 1 && id <= 117) {
            ItemStack item = new ItemStack(CppItems.CHARACTER);
            NbtCompound nbt = new NbtCompound();
            nbt.putInt("character", id);
            item.setTag(nbt);
            return item;
        }else return null;
    }
}
