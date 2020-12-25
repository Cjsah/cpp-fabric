package net.cpp.gui.handler;

import static net.minecraft.item.Items.LEATHER_BOOTS;
import static net.minecraft.item.Items.LEATHER_CHESTPLATE;
import static net.minecraft.item.Items.LEATHER_HELMET;
import static net.minecraft.item.Items.LEATHER_HORSE_ARMOR;
import static net.minecraft.item.Items.LEATHER_LEGGINGS;
import static net.minecraft.item.Items.WHITE_DYE;

import java.awt.Color;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.cpp.api.CodingTool;
import net.cpp.init.CppScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;

public class ColorPaletteScreenHandler extends ScreenHandler {
	private int rgb;
	public SimpleInventory items = new SimpleInventory(3) {
		public ItemStack addStack(ItemStack stack) {
			ItemStack r = super.addStack(stack);
			onContentChanged(items);
			return r;
		}

		public void setStack(int slot, ItemStack stack) {
			super.setStack(slot, stack);
			if (slot != 2)
				onContentChanged(items);
		}

		public ItemStack removeStack(int slot, int amount) {
			ItemStack r = super.removeStack(slot, amount);
			if (slot != 2)
				onContentChanged(items);
			return r;
		}

		public ItemStack removeStack(int slot) {
			ItemStack r = super.removeStack(slot);
			if (slot != 2)
				onContentChanged(items);
			return r;
		}
	};
	public DyeItem neededDye = (DyeItem) WHITE_DYE;
	private PlayerEntity player;

	public ColorPaletteScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(CppScreenHandler.COLOR_PALETTE, syncId);
		player = playerInventory.player;
		int m;
		int l;

		for (m = 0; m < 3; ++m) {
			for (l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
			}
		}

		for (m = 0; m < 9; ++m) {
			this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
		}
		for (int i = 0; i < 2; i++) {
			addSlot(new Slot(items, i, CodingTool.x(8), CodingTool.y(i)));
		}
		addSlot(new ResultSlot(items, 2, CodingTool.x(8), CodingTool.y(2)) {
			private int amount;

			@Override
			public boolean canTakeItems(PlayerEntity playerEntity) {
//				System.out.println(neededDye);
				return items.getStack(1).isOf(neededDye);
			}

			@Override
			public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
				onCrafted(stack);
				items.getStack(0).decrement(1);
				items.getStack(1).decrement(1);
				return stack;
			}

			public ItemStack takeStack(int amount) {
				if (this.hasStack()) {
					this.amount += Math.min(amount, this.getStack().getCount());
				}

				return super.takeStack(amount);
			}

			protected void onCrafted(ItemStack stack, int amount) {
				this.amount += amount;
				this.onCrafted(stack);
			}

			protected void onTake(int amount) {
				this.amount += amount;
			}

			protected void onCrafted(ItemStack stack) {
				if (this.amount > 0) {
					stack.onCraft(player.world, player, this.amount);
				}
				this.amount = 0;
			}
		});
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	public static class ColorPaletteSlot extends Slot {
		public static final Set<Item> INSERTABLE = ImmutableSet.of(LEATHER_BOOTS, LEATHER_CHESTPLATE, LEATHER_HELMET, LEATHER_HORSE_ARMOR, LEATHER_LEGGINGS);

		public ColorPaletteSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return INSERTABLE.contains(stack.getItem());
		}

		static {

		}
	}

	public void close(PlayerEntity player) {
		super.close(player);
		items.setStack(2, ItemStack.EMPTY);
		dropInventory(player, items);
	}

	public Item getNeededDye() {
		return neededDye;
	}

	public void setNeededDye(DyeItem neededDye) {
		this.neededDye = neededDye;
	}

	@Override
	public void onContentChanged(Inventory inventory) {
		if (items.getStack(0).getItem() instanceof DyeableItem) {
			ItemStack resultStack = items.getStack(0).copy();
			resultStack = DyeableItem.blendAndSetColor(resultStack, ImmutableList.of(neededDye));
			items.setStack(2, resultStack);
			if (player instanceof ServerPlayerEntity)
				((ServerPlayerEntity) player).networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 38, items.getStack(2)));
		} else {
			items.removeStack(2);
		}
//		System.out.println(player.world.isClient);
		super.onContentChanged(inventory);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack = slot.getStack();
			if (index >= 36) {
				this.insertItem(itemStack, 0, 36, true);
			} else if (index < 36) {
				if (itemStack.getItem() instanceof DyeableItem)
					insertItem(itemStack, 36, 37, false);
				else if (itemStack.getItem() instanceof DyeItem) {
					insertItem(itemStack, 37, 38, false);
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public void update(int rgb) {
		this.rgb = rgb;
		neededDye=getNearestDye(rgb);
		onContentChanged(items);
		
	}

	public static DyeItem getNearestDye(int rgb) {
		float[] frgb = new float[3];
		for (int i = 0; i < 3; i++)
			frgb[i] = ((rgb >> ((2 - i) * 8)) & 0xff) / 255f;
		double dis = Double.MAX_VALUE;
		int ix = 0;
		for (int i = 0; i < DyeColor.values().length; i++) {
			float[] dyergb = DyeColor.byId(i).getColorComponents();
			double dis0 = 0;
			for (int j = 0; j < 3; j++)
				dis0 += Math.pow(frgb[j] - dyergb[j], 2);
			if (dis0 < dis) {
				dis = dis0;
				ix = i;
			}
		}
		return DyeItem.byColor(DyeColor.byId(ix));
	}
	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		update(id>>2);
		return super.onButtonClick(player, id);
	}
}
