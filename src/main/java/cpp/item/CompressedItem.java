package cpp.item;

import cpp.api.Utils;
import cpp.init.CppItems;
import cpp.misc.ExperienceBottleHooks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

public class CompressedItem extends Item {
public CompressedItem(Settings settings) {
	super(settings);
}

public static int getMultiple(ItemStack compressed) {
	return compressed.hasTag() ? compressed.getTag().getInt(ExperienceBottleHooks.KEY) : 0;
}

public static void setMultiple(ItemStack compressed, int multiple) {
	compressed.getOrCreateTag().putInt(ExperienceBottleHooks.KEY, multiple);
}

@Override
@Environment(EnvType.CLIENT)
public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
	NbtCompound nbt = stack.getOrCreateTag();
	tooltip.add(new TranslatableText("tooltip.cpp.multiple", nbt.getByte("multiple")).formatted(Formatting.DARK_AQUA));
	List<Text> itemTooltip = ItemStack.fromNbt(nbt.getCompound("item")).getTooltip(null, context);
	tooltip.addAll(itemTooltip);
	if (context.isAdvanced())
		tooltip.add(LiteralText.EMPTY);
}

@Override
public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
	ItemStack itemStack = user.getStackInHand(hand);
	if (!world.isClient) {
		uncompressAndGive(user, itemStack);
	}
	return TypedActionResult.success(itemStack);
}

/**
 * 解压缩物品
 *
 * @param itemStack 要被解压的物品
 * @return 左对象是解压后的物品叠，右对象时解压后的物品叠的数量；如果不可解压，则左边是原来的物品叠，右边是1
 */
public static Pair<ItemStack, Integer> uncompress(ItemStack itemStack) {
	ItemStack result = itemStack;
	int count = 1;
	if (itemStack.isOf(CppItems.COMPRESSED_ITEM) || itemStack.isOf(Items.EXPERIENCE_BOTTLE)) {
		int multiple = itemStack.getOrCreateTag().getByte("multiple");
		if (multiple > 1 || itemStack.isOf(Items.EXPERIENCE_BOTTLE) && multiple > 0) {
			(result = itemStack.copy()).getOrCreateTag().putByte("multiple", (byte)(multiple - 1));
		} else if (multiple == 1 && itemStack.isOf(CppItems.COMPRESSED_ITEM)) {
			result = ItemStack.fromNbt(itemStack.getOrCreateTag().getCompound("item"));
		}
		result.setCount(result.getMaxCount());
		count = itemStack.getCount();
	}
	return new Pair<>(result, count);
}

public static boolean uncompressAndGive(PlayerEntity playerEntity, ItemStack compressed) {
	Pair<ItemStack, Integer> pair = uncompress(compressed);
	if (!(pair.getLeft() == compressed)) {
		if (playerEntity.isSneaking()) {
			ItemStack[] stacks = new ItemStack[pair.getRight()];
			Arrays.fill(stacks, pair.getLeft().copy());
			Utils.give(playerEntity, stacks);
			compressed.decrement(pair.getRight());
		} else {
			Utils.give(playerEntity, pair.getLeft());
			compressed.decrement(1);
		}
		return true;
	} else
		return false;
}

@Override
@Environment(EnvType.CLIENT)
public Text getName(ItemStack stack) {
	ItemStack itemStack = ItemStack.fromNbt(stack.getOrCreateTag().getCompound("item"));
	return new TranslatableText("tooltip.cpp.compressed").formatted(Formatting.DARK_AQUA).append(((MutableText)itemStack.getName()).formatted(itemStack.getRarity().formatting));
}
}
