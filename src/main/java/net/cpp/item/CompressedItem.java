package net.cpp.item;

import net.cpp.init.CppItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CompressedItem extends Item {
	public CompressedItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (!world.isClient) {
			Pair<ItemStack, Integer> uncompressed = uncompress(itemStack);
//			System.out.println(uncompressed.getRight());
			if (user.isSneaking()) {
				int i = uncompressed.getRight();
				while (i-- > 0)
					user.dropStack(uncompressed.getLeft().copy());
				itemStack.decrement(uncompressed.getRight());
			} else {
				user.dropStack(uncompressed.getLeft());
				itemStack.decrement(1);
			}
//			user.setStackInHand(hand, itemStack);
			return TypedActionResult.success(itemStack);
		}
		return TypedActionResult.pass(itemStack);
	}

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
