package net.cpp.screen.handler;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import net.cpp.api.CodingTool;
import net.cpp.init.CppItems;
import net.cpp.init.CppScreenHandler;
import net.cpp.screen.ColorPaletteScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.mixin.item.ItemStackMixin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;

import static net.minecraft.item.Items.*;

public class ColorPaletteScreenHandler extends ScreenHandler {
	/** 染料物品，用于调用染色方法（搞得跟静态方法一样，MJSB） */
	public static final DyeableItem DYER = new DyeableItem() {
	};
	public static final Identifier CHANNEL = Registry.ITEM.getId(CppItems.COLOR_PALETTE);
	public ColorPaletteScreen colorPaletteScreen;
	
	public int getRgb() {
		return rgb;
	}
	
	public void setRgb(int rgb) {
		this.rgb = rgb;
		updateResult();
		if (player.world.isClient()) {
			sendInts(rgb);
		}
	}
	
	private int rgb;
	
	public int getSelectedColorId(int i) {
		return selectedColorIds[i];
	}
	
	public void setSelectedColorId(int i, int value) {
		selectedColorIds[i] = value;
		updateResult();
		if (player.world.isClient()) {
			sendInts(i, value);
		}
	}
	
	private final int[] selectedColorIds = new int[3];
	private ItemStack rawStack = ItemStack.EMPTY;
	private static final int materialSlotIndex = 36;
	private static final int resultSlotIndex = 37;
	private static final int[] dyeSlotIndexes = {38, 39};
	/**
	 * 为调色盘量身定做的物品栏，考虑到了结果槽的特殊性，避免无限递归<br> 序号	用途<br> 0	原料<br> 1	产物<br> 2	第一染料<br> 3	第二染料<br> 4-19	模式3用于储存16色方块
	 */
	private final SimpleInventory items = new SimpleInventory(20) {
		public ItemStack addStack(ItemStack stack) {
			return super.addStack(stack);
		}
		
		public void setStack(int slot, ItemStack stack) {
			ItemStack preStack = getStack(slot);
			super.setStack(slot, stack);
			if (slot == 0) {
				if (!stack.isEmpty() && preStack.isEmpty() || !ItemStack.canCombine(preStack, stack)) {
					rawStack = stack;
					changeSlots();
					if (colorPaletteScreen != null) {
						colorPaletteScreen.appliedNewMaterial();
					}
				}
				updateResult();
			}
		}
		
		public ItemStack removeStack(int slot, int amount) {
			ItemStack r = super.removeStack(slot, amount);
			if (slot == 0 && !r.isEmpty()) {
				changeSlots();
				updateResult();
			} else if (slot == 1 && !r.isEmpty()) {
				getStack(2).decrement(1);
				if (getCurrentMode() == 2) {
					getStack(3).decrement(1);
				}
				items.removeStack(0);
			}
			return r;
		}
		
		public ItemStack removeStack(int slot) {
			ItemStack r = super.removeStack(slot);
			if (slot == 0 && !r.isEmpty()) {
				changeSlots();
				updateResult();
			} else if (slot == 1 && !r.isEmpty()) {
				getStack(2).decrement(1);
				if (getCurrentMode() == 2) {
					getStack(3).decrement(1);
				}
				items.removeStack(0);
			}
			return r;
		}
	};
	private DyeItem neededDye = (DyeItem) WHITE_DYE;
	private final PlayerEntity player;
	
	public ColorPaletteScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(CppScreenHandler.COLOR_PALETTE, syncId);
		player = playerInventory.player;
		if (player instanceof ServerPlayerEntity) {
			ServerPlayNetworking.registerReceiver(((ServerPlayerEntity) player).networkHandler, CHANNEL, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
				//				System.out.println(player.getName().getString());
				if (player.currentScreenHandler instanceof ColorPaletteScreenHandler) {
					//					System.out.println("" + getCurrentMode());
					ColorPaletteScreenHandler colorPaletteScreenHandler = (ColorPaletteScreenHandler) player.currentScreenHandler;
					switch (colorPaletteScreenHandler.getCurrentMode()) {
						case 1: {
							colorPaletteScreenHandler.rgb = buf.readInt();
							colorPaletteScreenHandler.updateResult();
							break;
						}
						case 2:
						case 3: {
							colorPaletteScreenHandler.selectedColorIds[buf.readInt()] = buf.readInt();
							colorPaletteScreenHandler.updateResult();
							break;
						}
					}
				}
			});
		}
		CodingTool.addPlayerSlots(this::addSlot, player.getInventory());
		addSlot(new Slot(items, 0, CodingTool.x(8), CodingTool.y(0)));
		changeSlots();
	}
	
	public DyeItem getNeededDye(int i) {
		switch (getCurrentMode()) {
			case 1:
				return neededDye;
			case 2:
				return DyeItem.byColor(DyeColor.byId(getSelectedColorId(i)));
			case 3:
				return DyeItem.byColor(DyeColor.byId(getSelectedColorId(2)));
		}
		return neededDye;
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
	
	public void close(PlayerEntity player) {
		super.close(player);
		CodingTool.give(player, items.removeStack(0));
	}
	
	@Override
	public void onContentChanged(Inventory inventory) {
		//		items.setStack(2, items.getStack(0).copy());
		//		items.getStack(2).setCount(1);
		//		if (items.getStack(0).getItem() instanceof DyeableItem) {
		//			DYER.setColor(items.getStack(2), rgb & 0x00ffffff);
		//		} else if (items.getStack(0).isOf(Items.FIREWORK_STAR)) {
		//
		//			List<Integer> list = Arrays.stream(items.getStack(2).getOrCreateSubTag("Explosion").getIntArray("Colors")).boxed().collect(Collectors.toList());
		//			list.add(rgb);
		//			items.getStack(2).getOrCreateSubTag("Explosion").putIntArray("Colors", list);
		//
		//		} else {
		//			items.removeStack(2);
		//		}
		super.onContentChanged(inventory);
		//		System.out.println(items);
	}
	
	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {//TODO
		ItemStack stack = getSlot(index).getStack();
		if (index >= 0 && index < 36) {
			if (getCurrentMode() > 0 && stack.getItem() instanceof DyeItem) {
				if (!insertItem(stack, 38, 39, false)) {
					if (getCurrentMode() == 2) {
						insertItem(stack, 39, 40, false);
					}
				}
			} else {
				insertItem(stack, 36, 37, false);
			}
		} else if (index == 36) {
			CodingTool.give(player, items.removeStack(0));
		} else if (index == 37) {
			if (getSlot(37).canTakeItems(player)) {
				CodingTool.give(player, items.removeStack(1));
			}
		} else {
			insertItem(stack, 0, 36, false);
		}
		return ItemStack.EMPTY;
	}
	
	/**
	 * 通过RGB获取最接近该颜色的染料
	 *
	 * @param rgb RGB色彩
	 *
	 * @return 最接近的染料物品
	 */
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
		return super.onButtonClick(player, id);
	}
	
	public ItemStack getMaterialStack() {
		return items.getStack(0);
	}
	
	public Slot getDyeSlot(int i) {
		return slots.get(38 + i);
	}
	
	public int getCurrentMode() {
		ItemStack stack = items.getStack(0);
		if (stack.isEmpty()) {
			return 0;
		}
		Item item = stack.getItem();
		if (item instanceof DyeableItem || item == Items.FIREWORK_STAR) {
			return 1;
		} else if (item == Items.TROPICAL_FISH_BUCKET) {
			return 2;
		} else {
			return 3;
		}
	}
	
	public void changeSlots() {
		while (slots.size() > 37) {
			slots.remove(slots.size() - 1);
		}
		int mode = getCurrentMode();
		if (mode > 0) {
			addSlot(new ResultSlot(items, 1, CodingTool.x(8), CodingTool.y(2)) {
				@Override
				public boolean canTakeItems(PlayerEntity playerEntity) {
					if (getCurrentMode() == 2) {
						if (!getDyeSlot(1).getStack().isOf(getNeededDye(1))) {
							return false;
						}
					}
					if (!getDyeSlot(0).getStack().isOf(getNeededDye(0))) {
						return false;
					}
					return true;
				}
				
				@Override
				public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
					return super.onTakeItem(player, stack);
				}
				
				@Override
				protected void onTake(int amount) {
					super.onTake(amount);
				}
			});
			addSlot(new Slot(items, 2, CodingTool.x(8), CodingTool.y(1)));
			if (mode == 2) {
				addSlot(new Slot(items, 3, CodingTool.x(7), CodingTool.y(1)));
			} else {
				CodingTool.give(player, items.removeStack(3));
				if (mode == 3) {
					for (int i = 0; i < 16; i++) {
						addSlot(new Slot(items, i + 4, CodingTool.x(i % 8), CodingTool.y(1 + i / 8)) {
							@Override
							public boolean canTakeItems(PlayerEntity playerEntity) {
								return false;
							}
							
							@Override
							public boolean canInsert(ItemStack stack) {
								return false;
							}
							
						});
					}
				}
			}
			
		} else {
			items.removeStack(1);
			for (int i = 2; i < 4; i++) {
				CodingTool.give(player, items.removeStack(i));
			}
		}
		
	}
	
	public void updateResult() {
		ItemStack materialStack = getMaterialStack();
		ItemStack resultStack = ItemStack.EMPTY;
		switch (getCurrentMode()) {
			case 1: {
				if (materialStack.getItem() instanceof DyeableItem) {
					resultStack = materialStack.copy();
					resultStack.getOrCreateSubTag("display").putInt("color", getRgb());
					neededDye = getNearestDye(rgb);
				} else if (materialStack.isOf(Items.FIREWORK_STAR)) {
					resultStack = materialStack.copy();
					int[] colors = resultStack.getOrCreateSubTag("Explosion").getIntArray("Colors");
					List<Integer> list = Lists.newLinkedList();
					for (int i : colors) {
						list.add(i);
					}
					list.add(getRgb());
					resultStack.getOrCreateSubTag("Explosion").putIntArray("Colors", list);
					neededDye = getNearestDye(rgb);
				}
				break;
			}
			case 2: {
				resultStack = materialStack.copy();
				int variant = resultStack.getOrCreateTag().getInt(ColorPaletteScreen.TROPICAL_BUCKET_NBT_KEY);
				variant &= 0xffff;
				variant |= getSelectedColorId(0) << 16;
				variant |= getSelectedColorId(1) << 24;
				resultStack.getOrCreateTag().putInt(ColorPaletteScreen.TROPICAL_BUCKET_NBT_KEY, variant);
				break;
			}
			case 3: {
				Identifier materialId = Registry.ITEM.getId(materialStack.getItem());
				String resultIdPath = materialId.getPath();
				DyeColor originalColor = null;
				for (DyeColor dyeColor : DyeColor.values()) {
					if (materialId.getPath().contains(dyeColor.asString())) {
						originalColor = dyeColor;
						resultIdPath = materialId.getPath().replace(dyeColor.asString(), "");
						break;
					}
				}
				if (originalColor == null) {
					resultIdPath = "_" + materialId.getPath();
				}
				if (resultStack != (resultStack = dyeStack(materialStack, DyeColor.byId(getSelectedColorId(2))))) {
					for (int i = 0; i < 16; i++) {
						ItemStack stack = dyeStack(materialStack,DyeColor.byId(i));
						stack.setCount(1);
						items.setStack(i + 4, stack);
					}
				}
			}
		}
		items.setStack(1, resultStack);
	}
	
	@Environment(EnvType.CLIENT)
	public void sendInts(int... ints) {
		PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
		for (int i : ints) {
			packetByteBuf.writeInt(i);
		}
		ClientPlayNetworking.send(CHANNEL, packetByteBuf);
	}
	
	@Nullable
	public static DyeColor getDyeColor(Item item) {
		Identifier id = Registry.ITEM.getId(item);
		DyeColor color = null;
		for (DyeColor dyeColor : DyeColor.values()) {
			if (id.getPath().contains(dyeColor.asString())) {
				color = dyeColor;
				break;
			}
		}
		return color;
	}
	
	public static ItemStack removeColor(ItemStack stack) {
		ItemStack resultStack = stack;
		Identifier id = Registry.ITEM.getId(stack.getItem());
		String path = null;
		for (DyeColor dyeColor : DyeColor.values()) {
			if (id.getPath().contains(dyeColor.asString())) {
				path = id.getPath().replace(dyeColor.asString() + "_", "");
				break;
			}
		}
		if (path != null) {
			Item item = Registry.ITEM.get(new Identifier(id.getNamespace(), path));
			if (item == AIR) {
				path = path.replace("stained_", "");
				item = Registry.ITEM.get(new Identifier(id.getNamespace(), path));
			}
			if (item != AIR) {
				resultStack = new ItemStack(item, resultStack.getCount());
				resultStack.setTag(stack.getTag());
			}
		}
		return resultStack;
	}
	
	public static ItemStack dyeStack(ItemStack stack, DyeColor color) {
		ItemStack resultStack = stack;
		Identifier id = Registry.ITEM.getId(removeColor(stack).getItem());
		String path = color.asString() + "_" + id.getPath();
		Item item = Registry.ITEM.get(new Identifier(id.getNamespace(), path));
		if (item == AIR) {
			path = color.asString() + "_stained_" + id.getPath();
			item = Registry.ITEM.get(new Identifier(id.getNamespace(), path));
		}
		if (item != AIR) {
			resultStack = new ItemStack(item, resultStack.getCount());
			resultStack.setTag(stack.getTag());
		}
		return resultStack;
	}
}
