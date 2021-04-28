package cpp.item;

import com.google.common.collect.ImmutableList;
import cpp.init.CppItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtCompound;
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
    @Environment(EnvType.CLIENT)
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
                stacks.add(get(i));
            }
        }
    }

    public static ItemStack randomGetOne() {
        ItemStack item = new ItemStack(CppItems.ANCIENT_SCROLL);
        NbtCompound nbt = new NbtCompound();
        nbt.putString("enchantment", list.get(new Random().nextInt(list.size())));
        item.setTag(nbt);
        return item;
    }

    public static ItemStack get(String name) {
        if (list.contains(name)) {
            ItemStack item = new ItemStack(CppItems.ANCIENT_SCROLL);
            NbtCompound nbt = new NbtCompound();
            nbt.putString("enchantment", name);
            item.setTag(nbt);
            return item;
        }else return null;
    }
}
