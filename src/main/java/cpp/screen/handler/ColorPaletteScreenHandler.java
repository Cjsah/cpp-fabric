package cpp.screen.handler;

import java.util.*;

import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import cpp.api.Utils;
import cpp.init.CppItems;
import cpp.init.CppScreenHandler;
import cpp.screen.ColorPaletteScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
//	private static final int resultSlotIndex = 37;
//	private static final int[] dyeSlotIndexes = {38, 39};
	/**
	 * 为调色盘量身定做的物品栏，考虑到了结果槽的特殊性，避免无限递归<br> 序号	用途<br> 0	原料<br> 1	产物<br> 2	第一染料<br> 3	第二染料<br> 4-19	模式3用于储存16色方块
	 */
	private final SimpleInventory items = new SimpleInventory(20) /*{//DEL
		public ItemStack addStack(ItemStack stack) {
			return super.addStack(stack);
		}
		
		public void setStack(int slot, ItemStack stack) {//DEL
			super.setStack(slot, stack);
		}
		
		public ItemStack removeStack(int slot, int amount) {//DEL
			return super.removeStack(slot, amount);
		}
		
		public ItemStack removeStack(int slot) {//DEL
			return super.removeStack(slot);
		}
	}*/;
	private Item neededDye = WHITE_DYE;
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
		Utils.addPlayerSlots(this::addSlot, player.getInventory());
		addSlot(new Slot(items, 0, Utils.x(8), Utils.y(0)) {
			@Override
			public void onStackChanged(ItemStack originalItem, ItemStack newItem) {
				super.onStackChanged(originalItem, newItem);
				//				onMaterialChanged();
			}
			
			@Override
			public void setStack(ItemStack stack) {
				super.setStack(stack);
				onMaterialChanged();
			}
			
			@Override
			public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
				ItemStack r = super.onTakeItem(player, stack);
				onMaterialChanged();
				return r;
			}
			
			@Override
			protected void onTake(int amount) {
				super.onTake(amount);
			}
		});
		changeSlots();
	}
	
	public Item getNeededDye(int i) {
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
		insertItem(items.getStack(0), 0, 36, false);
		Utils.give(player, items.removeStack(0));
		onMaterialChanged();
	}
	
//	@Override
//	public void onContentChanged(Inventory inventory) {
//		super.onContentChanged(inventory);
//	}
	
	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		if (getSlot(index).canTakeItems(player)) {
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
					onMaterialChanged();
				}
			} else if (index == 36) {
				insertItem(stack, 0, 36, false);
				onMaterialChanged();
			} else if (index == 37) {
				insertItem(stack, 0, 36, false);
				onResultTaken();
			} else {
				insertItem(stack, 0, 36, false);
			}
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
		int mode = getCurrentMode();
		if (mode > 0) {
			addSlot(new ResultSlot(items, 1, Utils.x(8), Utils.y(2)) {
				@Override
				public boolean canTakeItems(PlayerEntity playerEntity) {
					if (getCurrentMode() == 2) {
						if (!getDyeSlot(1).getStack().isOf(getNeededDye(1))) {
							return false;
						}
					}
					return getDyeSlot(0).getStack().isOf(getNeededDye(0));
				}
				
				@Override
				public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
					ItemStack r = super.onTakeItem(player, stack);
					onResultTaken();
					return r;
				}
				
//				@Override
//				protected void onTake(int amount) {
//					super.onTake(amount);
//				}
			});
			addSlot(new Slot(items, 2, Utils.x(8), Utils.y(1)));
			if (mode == 2) {
				addSlot(new Slot(items, 3, Utils.x(7), Utils.y(1)));
			} else {
				insertItem(items.getStack(3), 0, 36, false);
				Utils.give(player, items.removeStack(3));
				while (slots.size() > 40) {
					slots.remove(slots.size() - 1);
				}
				if (mode == 3) {
					class DisplaySlot extends Slot {
						
						public DisplaySlot(Inventory inventory, int index, int x, int y) {
							super(inventory, index, x, y);
						}
						@Override
						public boolean canTakeItems(PlayerEntity playerEntity) {
							return false;
						}
						
						@Override
						public boolean canInsert(ItemStack stack) {
							return false;
						}
					}
					for (int i = 0; i < 16; i++) {
						addSlot(new DisplaySlot(items, i + 4, Utils.x(i % 8), Utils.y(1 + i / 8)));
					}
//					addSlot(new DisplaySlot(items, 21, CodingTool.x(8), CodingTool.y(0)));
				}else{
					while (slots.size() > 39) {
						slots.remove(slots.size() - 1);
					}
				}
			}
		} else {
			items.removeStack(1);
			for (int i = 2; i < 4; i++) {
				insertItem(items.getStack(i), 0, 36, false);
				Utils.give(player, items.removeStack(i));
			}
			while (slots.size() > 37) {
				slots.remove(slots.size() - 1);
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
				resultStack = dyeStack(materialStack, DyeColor.byId(getSelectedColorId(2)));
				for (int i = 0; i < 16; i++) {
					ItemStack stack = dyeStack(materialStack, DyeColor.byId(i));
					stack.setCount(1);
					items.setStack(i + 4, stack);
				}
//				ItemStack stack = removeColor(materialStack);
//				stack.setCount(1);
//				items.setStack(20, stack);
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
			if (id.getPath().indexOf(dyeColor.asString()) == 0) {
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
			if (id.getPath().indexOf(dyeColor.asString()) == 0) {
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
				resultStack = new ItemStack(item, stack.getCount());
				resultStack.setTag(stack.getTag());
			}
		}
		return resultStack;
	}
	
	public static ItemStack dyeStack(ItemStack stack, DyeColor color) {
		ItemStack resultStack = ItemStack.EMPTY;
		Identifier id = Registry.ITEM.getId(stack.getItem());
		String rootPath = null;
		for (DyeColor dyeColor : DyeColor.values()) {
			if (id.getPath().indexOf(dyeColor.asString()) == 0) {
				rootPath = id.getPath().replace(dyeColor.asString(), "");
			}
		}
		if (rootPath == null) {
			rootPath = "_" + id.getPath();
		}
		String path = color.asString() + rootPath;
		Item item = Registry.ITEM.get(new Identifier(id.getNamespace(), path));
		if (item == AIR) {
			path = color.asString() + "_stained" + rootPath;
			item = Registry.ITEM.get(new Identifier(id.getNamespace(), path));
		}
		if (item != AIR) {
			resultStack = new ItemStack(item, stack.getCount());
			resultStack.setTag(stack.getTag());
		}
		return resultStack;
	}
	
	public void onMaterialChanged() {
		changeSlots();
		if (colorPaletteScreen != null) {
			colorPaletteScreen.appliedNewMaterial();
		}
		updateResult();
	}
	
	public void onResultTaken() {
		items.getStack(2).decrement(1);
		if (getCurrentMode() == 2) {
			items.getStack(3).decrement(1);
		}
		items.removeStack(0);
		changeSlots();
	}
}
