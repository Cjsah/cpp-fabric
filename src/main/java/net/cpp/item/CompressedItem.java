package net.cpp.item;

import java.util.List;

import net.cpp.init.CppItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CompressedItem extends Item {
	public CompressedItem(Settings settings) {
		super(settings);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		CompoundTag tag = stack.getOrCreateTag();
		tooltip.add(new TranslatableText(ItemStack.fromTag(tag.getCompound("item")).getTranslationKey()).formatted(Formatting.YELLOW));
		tooltip.add(new TranslatableText("tooltip.cpp.multiple", tag.getByte("multiple")).formatted(Formatting.DARK_AQUA));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (!world.isClient) {
			Pair<ItemStack, Integer> uncompressed = uncompress(itemStack);
			if (user.isSneaking()) {
				int i = uncompressed.getRight();
				while (i-- > 0)
					user.dropStack(uncompressed.getLeft().copy());
				itemStack.decrement(uncompressed.getRight());
			} else {
				user.dropStack(uncompressed.getLeft());
				itemStack.decrement(1);
			}
			return TypedActionResult.success(itemStack);
		}
		return TypedActionResult.pass(itemStack);
	}

	/**
	 * 解压缩物品
	 * 
	 * @param itemStack 要被解压的物品
	 * @return Pair<ItemStack, Integer>，左对象是解压后的物品叠，右对象时解压后的物品叠的数量；如果不可解压，则左边是原来的物品叠，右边是1
	 */
	public static Pair<ItemStack, Integer> uncompress(ItemStack itemStack) {
		ItemStack result = itemStack;
		int count = 1;
		if (itemStack.isOf(CppItems.COMPRESSED_ITEM) || itemStack.isOf(Items.EXPERIENCE_BOTTLE)) {
			int multiple = itemStack.getOrCreateTag().getByte("multiple");
			if (multiple > 1 || itemStack.isOf(Items.EXPERIENCE_BOTTLE) && multiple > 0) {
				(result = itemStack.copy()).getOrCreateTag().putByte("multiple", (byte) (multiple - 1));
			} else if (multiple == 1 && itemStack.isOf(CppItems.COMPRESSED_ITEM)) {
				result = ItemStack.fromTag(itemStack.getOrCreateTag().getCompound("item"));
			}
			result.setCount(result.getMaxCount());
			count = itemStack.getCount();
		}
		return new Pair<>(result, count);
	}

}
