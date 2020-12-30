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
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.Rarity;
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
		tooltip.add(new TranslatableText("tooltip.cpp.multiple", tag.getByte("multiple")).formatted(Formatting.DARK_AQUA));
		List<Text> itemTooltip = ItemStack.fromTag(tag.getCompound("item")).getTooltip(null, context);
//		itemTooltip.remove(0);
		tooltip.addAll(itemTooltip);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (!world.isClient && uncompressAndDrop(user, itemStack)) {
			return TypedActionResult.success(itemStack);
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
				(result = itemStack.copy()).getOrCreateTag().putByte("multiple", (byte) (multiple - 1));
			} else if (multiple == 1 && itemStack.isOf(CppItems.COMPRESSED_ITEM)) {
				result = ItemStack.fromTag(itemStack.getOrCreateTag().getCompound("item"));
			}
			result.setCount(result.getMaxCount());
			count = itemStack.getCount();
		}
		return new Pair<>(result, count);
	}

	/**
	 * 获取压缩附魔之瓶的经验值
	 * 
	 * @param compressedExpBottle 压缩附魔之瓶
	 * @return 一瓶的经验值
	 */
	public static int getCompressedExp(ItemStack compressedExpBottle) {
		return 9 << (6 * compressedExpBottle.getOrCreateTag().getByte("multiple"));
	}

	public static boolean uncompressAndDrop(PlayerEntity playerEntity, ItemStack compressed) {
		Pair<ItemStack, Integer> pair = uncompress(compressed);
		if (!(pair.getLeft() == compressed)) {
			if (playerEntity.isSneaking()) {
				int i = pair.getRight();
				while (i-- > 0)
					playerEntity.dropStack(pair.getLeft().copy());
				compressed.decrement(pair.getRight());
			} else {
				playerEntity.dropStack(pair.getLeft());
				compressed.decrement(1);
			}
			return true;
		} else
			return false;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Text getName(ItemStack stack) {
		ItemStack itemStack = ItemStack.fromTag(stack.getOrCreateTag().getCompound("item"));
		return new TranslatableText("tooltip.cpp.compressed").formatted(Formatting.DARK_AQUA).append(((MutableText) itemStack.getName()).formatted(itemStack.getRarity().formatting));
	}
}
