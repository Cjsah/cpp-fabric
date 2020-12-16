package net.cpp.block.entity;

import static net.cpp.init.CppItems.FRUIT_LEAVES;
import static net.cpp.init.CppItems.FRUIT_SAPLING;
import static net.cpp.init.CppItems.GRAFTER;
import static net.cpp.init.CppItems.GREEN_FORCE_OF_WATER;
import static net.cpp.init.CppItems.ORE_LEAVES;
import static net.cpp.init.CppItems.ORE_SAPLING;
import static net.cpp.init.CppItems.RED_FORCE_OF_FIRE;
import static net.cpp.init.CppItems.SAKURA_LEAVES;
import static net.cpp.init.CppItems.SAKURA_SAPLING;
import static net.cpp.init.CppItems.SPLINT;
import static net.cpp.init.CppItems.WOOL_LEAVES;
import static net.cpp.init.CppItems.WOOL_SAPLING;
import static net.minecraft.item.Items.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.cpp.gui.handler.ItemProcessorScreenHandler;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppBlocks;
import net.cpp.init.CppItems;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class ItemProcessorBlockEntity extends AMachineBlockEntity  {
	public static final Set<Item> LEATHERS = new HashSet<>(
			Arrays.asList(LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS));
	public static final Map<Item, Map<Item, ItemStackAndCount>> RECIPES = new HashMap<>();
	public static final Set<BlockItem> ORES;
	private static final int[] AVAILABLE_SLOTS = new int[] { 0, 1 };
	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
	private int lastTickCount = -1;
	public final PropertyDelegate propertyDelegate = new OutputDirectionPropertyDelegate();

	public ItemProcessorBlockEntity() {
		super(CppBlockEntities.ITEM_PROCESSER);
	}

	@Override
	public PropertyDelegate getPropertyDelegate() {
		return propertyDelegate;
	}
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		Inventories.fromTag(tag, inventory);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		Inventories.toTag(tag, inventory);
		return tag;
	}

	@Override
	public void tick() {
		// XXX 代码结构需要优化
		if (!world.isClient && !getStack(0).isEmpty() && !getStack(1).isEmpty()) {
			Item tool = getStack(0).getItem();
			ItemStack input1 = getStack(1), output1 = getStack(2);
			if (tool == RED_FORCE_OF_FIRE) {
// 红色火之力
				SmeltingRecipe recipe = world.getRecipeManager()
						.getFirstMatch(RecipeType.SMELTING, new SimpleInventory(new ItemStack[] { input1 }), this.world)
						.orElse(null);
				if (recipe != null) {
					ItemStack result = recipe.getOutput();
					boolean processed = false;
					if (processed = output1.isEmpty())
						setStack(2, result.copy());
					else if (processed = output1.isItemEqual(result)
							&& output1.getCount() + result.getCount() <= output1.getMaxCount())
						output1.increment(result.getCount());
					if (processed)
						input1.decrement(1);
				}
			} else if (tool == CppItems.COMPRESSOR) {
// 压缩器
				if (input1.getCount() >= input1.getMaxCount()
						&& (output1.isEmpty() || output1.getCount() < output1.getMaxCount())) {
					ItemStack output;
					if (input1.getItem() == CppItems.COMPRESSED_ITEM
							|| input1.getItem() == CppItems.COMPRESSED_EXPERIENCE_BOTTLE) {
						output = input1.copy();
						output.setCount(1);
						output.getOrCreateTag().putByte("mutiple",
								(byte) (input1.getOrCreateTag().getByte("mutiple") + 1));
					} else {
						output = new ItemStack(
								input1.getItem() == EXPERIENCE_BOTTLE ? CppItems.COMPRESSED_EXPERIENCE_BOTTLE
										: CppItems.COMPRESSED_ITEM);
						CompoundTag inputItemNBT = itemStackToTag(input1);
						inputItemNBT.remove("Slot");
						inputItemNBT.remove("Count");
						output.putSubTag("item", inputItemNBT);
						output.getTag().putByte("mutiple", (byte) 1);
					}
					boolean ed = false;
					if (ed = output1.isEmpty())
						setStack(2, output);
					else if (ed = ItemStack.areItemsEqual(output1, output) && ItemStack.areTagsEqual(output1, output))
						output1.increment(1);
					if (ed)
						input1.decrement(input1.getMaxCount());
				}
			}else if (tool == BONE_MEAL) {
				if (input1.getItem() == NETHERRACK) {
					Block block = world.getBlockState(pos.up()).getBlock();
					Set<Block> tmpSet = new HashSet<>(Arrays.asList(Blocks.CRIMSON_NYLIUM,Blocks.WARPED_NYLIUM));
					if (tmpSet.contains(block) && canInsert(2, new ItemStack(block))) {
						insert(2, new ItemStack(block));
						getStack(1).decrement(1);
						getStack(0).decrement(1);
					}
				}
			} else if (tool instanceof MiningToolItem && tool.isIn(FabricToolTags.PICKAXES)
					&& ORES.contains(getStack(1).getItem())) {
				BlockState blockState = ((BlockItem) getStack(1).getItem()).getBlock().getDefaultState();
				if (tool.isEffectiveOn(blockState)) {
					List<ItemStack> list = blockState.getDroppedStacks(new LootContext.Builder((ServerWorld) world)
							.parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
							.parameter(LootContextParameters.TOOL, getStack(0)));
					boolean able = true;
					if (list.size() > 2)
						able = false;
					else
						for (int i = 0; i < list.size(); i++) {
							if (!getStack(i + 2).isEmpty() && (!ItemStack.areItemsEqual(getStack(i + 2), list.get(i))
									|| getStack(i + 2).getCount() + list.get(i).getCount() > getStack(i + 2)
											.getMaxCount())) {
								able = false;
								break;
							}
						}
					if (able) {
						getStack(0).damage(1, world.random, null);
						getStack(1).decrement(1);
						for (int i = 0; i < list.size(); i++) {
							if (getStack(i + 2).isEmpty())
								setStack(i + 2, list.get(i));
							else
								getStack(i + 2).increment(list.get(i).getCount());
						}
					}
				}
			} else {
				ItemStackAndCount itemStackAndCount = RECIPES.getOrDefault(tool, Collections.emptyMap())
						.get(input1.getItem());
				if (itemStackAndCount != null && input1.getCount() >= itemStackAndCount.count) {
					boolean used = false;
					if (input1.getItem() == GILDED_BLACKSTONE) {
						if (lastTickCount == -1)
							lastTickCount = 2 + (int) (4 * Math.random());
						if ((output1.isEmpty() || output1.getItem() == GOLD_NUGGET
								&& output1.getCount() + lastTickCount <= output1.getMaxCount())) {
							input1.decrement(1);
							if (output1.isEmpty())
								setStack(2, new ItemStack(GOLD_NUGGET, lastTickCount));
							else
								output1.increment(lastTickCount);
							used = true;
							lastTickCount = -1;
						}
					} else if ((output1.isEmpty() || ItemStack.areItemsEqual(itemStackAndCount.itemStack, output1)
							&& output1.getCount() + itemStackAndCount.itemStack.getCount() <= output1.getMaxCount())) {
						Item result1 = itemStackAndCount.itemStack.getItem();
						ItemStack output2 = getStack(3);
						boolean able = true;
						if (LEATHERS.contains(input1.getItem())) {
							if (output1.isEmpty()) {
								output1 = input1.copy();
								output1.getOrCreateSubTag("display").remove("color");
								setStack(2, output1);
								input1.decrement(1);
							}
						} else {
							if (result1 == CARVED_PUMPKIN) {
								if (output2.isEmpty() || output2.getItem() == PUMPKIN_SEEDS
										&& output2.getCount() + 4 <= output2.getMaxCount()) {
									if (output2.isEmpty())
										setStack(3, new ItemStack(PUMPKIN_SEEDS, 4));
									else
										output2.increment(4);
								} else
									able = false;
							} else if (result1 == OBSIDIAN) {
								if (output2.isEmpty() || output2.getItem() == BUCKET
										&& output2.getCount() + 1 <= output2.getMaxCount()) {
									if (output2.isEmpty())
										setStack(3, new ItemStack(BUCKET, 1));
									else
										output2.increment(1);
								} else
									able = false;
							}
							if (able) {
								input1.decrement(itemStackAndCount.count);
								if (output1.isEmpty())
									setStack(2, itemStackAndCount.itemStack.copy());
								else
									output1.increment(itemStackAndCount.itemStack.getCount());
								used = true;
							}
						}
					}
					if (used && getStack(0).damage(1, world.random, null)) {
						getStack(0).decrement(1);
					}
				}
			}
			for (int i : new int[] { 2, 3 })
				if (!getStack(i).isEmpty()) {
					setStack(i, output(getStack(i)));
				}
		}
	}

	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return inventory;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		inventory = list;
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new ItemProcessorScreenHandler(syncId, playerInventory, this);
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return AVAILABLE_SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return slot == 0 && stack != null && RECIPES.containsKey(stack.getItem()) || slot == 1;
	}


	public boolean isSmeltable(ItemStack itemStack) {
		return this.world.getRecipeManager()
				.getFirstMatch(RecipeType.SMELTING, new SimpleInventory(new ItemStack[] { itemStack }), this.world)
				.isPresent();
	}

	public static void put(Map<Item, ItemStackAndCount> map, Item item1, Item item2) {
		put(map, item1, item2, 1);
	}

	public static void put(Map<Item, ItemStackAndCount> map, Item item1, Item item2, int count2) {
		put(map, item1, 1, item2, count2);
	}

	public static void put(Map<Item, ItemStackAndCount> map, Item item1, int count1, Item item2) {
		put(map, item1, count1, item2, 1);
	}

	public static void put(Map<Item, ItemStackAndCount> map, Item item1, int count1, Item item2, int count2) {
		map.put(item1, new ItemStackAndCount(new ItemStack(item2, count2), count1));
	}

	static {
		{
			Map<Item, ItemStackAndCount> map, marker = Collections.emptyMap();

			for (Item item : FabricToolTags.SHOVELS.values()) {
				RECIPES.put(item, marker);
			}
			map = new HashMap<>();
			put(map, GRASS_BLOCK, DIRT);
			put(map, MYCELIUM, DIRT);
			put(map, PODZOL, DIRT);
			put(map, GRAVEL, FLINT);
			put(map, GILDED_BLACKSTONE, GOLD_NUGGET, 2);
			put(map, CLAY, CLAY_BALL, 4);
			put(map, SNOW_BLOCK, SNOWBALL, 4);
			RECIPES.put(STONE_SHOVEL, map);

			for (Item item : FabricToolTags.HOES.values()) {
				RECIPES.put(item, marker);
			}
			map = new HashMap<>();
			put(map, COARSE_DIRT, DIRT);
			RECIPES.put(STONE_HOE, map);

			for (Item item : FabricToolTags.PICKAXES.values()) {
				RECIPES.put(item, marker);
			}
			map = new HashMap<>();
			put(map, STONE, COBBLESTONE);
			put(map, CRIMSON_NYLIUM, NETHERRACK);
			put(map, WARPED_NYLIUM, NETHERRACK);
			put(map, GLOWSTONE, GLOWSTONE_DUST, 4);
			RECIPES.put(STONE_PICKAXE, map);

			for (Item item : FabricToolTags.SHEARS.values()) {
				RECIPES.put(item, marker);
			}
			map = new HashMap<>();
			put(map, PUMPKIN, CARVED_PUMPKIN);
			RECIPES.put(SHEARS, map);

			for (Item item : FabricToolTags.AXES.values()) {
				RECIPES.put(item, marker);
			}
			map = new HashMap<>();
			put(map, MELON, MELON_SLICE, 9);
			put(map, ACACIA_LOG, STRIPPED_ACACIA_LOG);
			put(map, ACACIA_WOOD, STRIPPED_ACACIA_WOOD);
			put(map, BIRCH_LOG, STRIPPED_BIRCH_LOG);
			put(map, BIRCH_WOOD, STRIPPED_BIRCH_WOOD);
			put(map, CRIMSON_STEM, STRIPPED_CRIMSON_STEM);
			put(map, CRIMSON_HYPHAE, STRIPPED_CRIMSON_HYPHAE);
			put(map, DARK_OAK_LOG, STRIPPED_DARK_OAK_LOG);
			put(map, DARK_OAK_WOOD, STRIPPED_DARK_OAK_WOOD);
			put(map, JUNGLE_LOG, STRIPPED_JUNGLE_LOG);
			put(map, JUNGLE_WOOD, STRIPPED_JUNGLE_WOOD);
			put(map, OAK_LOG, STRIPPED_OAK_LOG);
			put(map, OAK_WOOD, STRIPPED_OAK_WOOD);
			put(map, SPRUCE_LOG, STRIPPED_SPRUCE_LOG);
			put(map, SPRUCE_WOOD, STRIPPED_SPRUCE_WOOD);
			put(map, WARPED_STEM, STRIPPED_WARPED_STEM);
			put(map, WARPED_HYPHAE, STRIPPED_WARPED_HYPHAE);
			RECIPES.put(STONE_AXE, map);

			map = new HashMap<>();
			put(map, ACACIA_LEAVES, ACACIA_SAPLING);
			put(map, BIRCH_LEAVES, BIRCH_SAPLING);
			put(map, DARK_OAK_LEAVES, DARK_OAK_SAPLING);
			put(map, JUNGLE_LEAVES, JUNGLE_SAPLING);
			put(map, OAK_LEAVES, OAK_SAPLING);
			put(map, SPRUCE_LEAVES, SPRUCE_SAPLING);
			put(map, FRUIT_LEAVES, FRUIT_SAPLING);
			put(map, ORE_LEAVES, ORE_SAPLING);
			put(map, SAKURA_LEAVES, SAKURA_SAPLING);
			put(map, WOOL_LEAVES, WOOL_SAPLING);
			put(map, NETHER_WART_BLOCK, CRIMSON_FUNGUS);
			put(map, WARPED_WART_BLOCK, WARPED_FUNGUS);
			put(map, BROWN_MUSHROOM_BLOCK, BROWN_MUSHROOM);
			put(map, RED_MUSHROOM_BLOCK, RED_MUSHROOM);
			RECIPES.put(GRAFTER, map);

			map = new HashMap<>();
			put(map, WHEAT, 9, HAY_BLOCK);
			put(map, MELON_SLICE, 9, MELON);
			put(map, DRIED_KELP, 9, DRIED_KELP_BLOCK);
			put(map, NETHER_WART, 9, NETHER_WART_BLOCK);
			put(map, SLIME_BALL, 9, SLIME_BLOCK);
			put(map, BONE_MEAL, 9, BONE_BLOCK);
			put(map, SPLINT, 9, DIAMOND);
			put(map, COAL, 9, COAL_BLOCK);
			put(map, IRON_INGOT, 9, IRON_BLOCK);
			put(map, GOLD_INGOT, 9, GOLD_BLOCK);
			put(map, REDSTONE, 9, REDSTONE_BLOCK);
			put(map, LAPIS_LAZULI, 9, LAPIS_BLOCK);
			put(map, DIAMOND, 9, DIAMOND_BLOCK);
			put(map, EMERALD, 9, EMERALD_BLOCK);
			put(map, IRON_NUGGET, 9, IRON_INGOT);
			put(map, GOLD_NUGGET, 9, GOLD_INGOT);
			put(map, NETHERITE_INGOT, 9, NETHERITE_BLOCK);
			put(map, RABBIT_HIDE, 4, LEATHER);
			put(map, SNOWBALL, 4, SNOW_BLOCK);
			put(map, GLOWSTONE_DUST, 4, GLOWSTONE);
			put(map, CLAY_BALL, 4, CLAY);
			put(map, QUARTZ, 4, QUARTZ_BLOCK);
			RECIPES.put(PISTON, map);

			map = new HashMap<>();
			for (Map.Entry<Item, Item> entry : CppItems.SEEDS_TO_FLOWERS.entrySet())
				put(map, entry.getValue(), entry.getKey(), 3);
			RECIPES.put(CppBlocks.CRAFTING_MACHINE.asItem(), map);

			RECIPES.put(RED_FORCE_OF_FIRE, new HashMap<>());

			map = new HashMap<>();
			for (Item item : new Item[] { WHITE_STAINED_GLASS, ORANGE_STAINED_GLASS, MAGENTA_STAINED_GLASS,
					LIGHT_BLUE_STAINED_GLASS, YELLOW_STAINED_GLASS, LIME_STAINED_GLASS, PINK_STAINED_GLASS,
					GRAY_STAINED_GLASS, LIGHT_GRAY_STAINED_GLASS, CYAN_STAINED_GLASS, PURPLE_STAINED_GLASS,
					BLUE_STAINED_GLASS, BROWN_STAINED_GLASS, GREEN_STAINED_GLASS, RED_STAINED_GLASS,
					BLACK_STAINED_GLASS })
				put(map, item, GLASS);
			for (Item item : new Item[] { WHITE_TERRACOTTA, ORANGE_TERRACOTTA, MAGENTA_TERRACOTTA,
					LIGHT_BLUE_TERRACOTTA, YELLOW_TERRACOTTA, LIME_TERRACOTTA, PINK_TERRACOTTA, GRAY_TERRACOTTA,
					LIGHT_GRAY_TERRACOTTA, CYAN_TERRACOTTA, PURPLE_TERRACOTTA, BLUE_TERRACOTTA, BROWN_TERRACOTTA,
					GREEN_TERRACOTTA, RED_TERRACOTTA, BLACK_TERRACOTTA })
				put(map, item, TERRACOTTA);
			for (Item item : new Item[] { WHITE_WOOL, ORANGE_WOOL, MAGENTA_WOOL, LIGHT_BLUE_WOOL, YELLOW_WOOL,
					LIME_WOOL, PINK_WOOL, GRAY_WOOL, LIGHT_GRAY_WOOL, CYAN_WOOL, PURPLE_WOOL, BLUE_WOOL, BROWN_WOOL,
					GREEN_WOOL, RED_WOOL, BLACK_WOOL })
				put(map, item, WHITE_WOOL);
			for (Item item : new Item[] { WHITE_BED, ORANGE_BED, MAGENTA_BED, LIGHT_BLUE_BED, YELLOW_BED, LIME_BED,
					PINK_BED, GRAY_BED, LIGHT_GRAY_BED, CYAN_BED, PURPLE_BED, BLUE_BED, BROWN_BED, GREEN_BED, RED_BED,
					BLACK_BED })
				put(map, item, WHITE_BED);
			put(map, SPONGE, WET_SPONGE);
			put(map, LAVA_BUCKET, OBSIDIAN);
			put(map, STICKY_PISTON, PISTON);
			put(map, FILLED_MAP, MAP);
			put(map, GLASS_BOTTLE, POTION);
			{
				Item[] powders = { WHITE_CONCRETE_POWDER, ORANGE_CONCRETE_POWDER, MAGENTA_CONCRETE_POWDER,
						LIGHT_BLUE_CONCRETE_POWDER, YELLOW_CONCRETE_POWDER, LIME_CONCRETE_POWDER, PINK_CONCRETE_POWDER,
						GRAY_CONCRETE_POWDER, LIGHT_GRAY_CONCRETE_POWDER, CYAN_CONCRETE_POWDER, PURPLE_CONCRETE_POWDER,
						BLUE_CONCRETE_POWDER, BROWN_CONCRETE_POWDER, GREEN_CONCRETE_POWDER, RED_CONCRETE_POWDER,
						BLACK_CONCRETE_POWDER },
						concretes = { WHITE_CONCRETE, ORANGE_CONCRETE, MAGENTA_CONCRETE, LIGHT_BLUE_CONCRETE,
								YELLOW_CONCRETE, LIME_CONCRETE, PINK_CONCRETE, GRAY_CONCRETE, LIGHT_GRAY_CONCRETE,
								CYAN_CONCRETE, PURPLE_CONCRETE, BLUE_CONCRETE, BROWN_CONCRETE, GREEN_CONCRETE,
								RED_CONCRETE, BLACK_CONCRETE };
				for (int i = 0; i < powders.length; i++)
					put(map, powders[i], concretes[i]);
			}
			for (Item item : new Item[] { LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS })
				put(map, item, item);
			for (Item item : new Item[] { WHITE_BANNER, ORANGE_BANNER, MAGENTA_BANNER, LIGHT_BLUE_BANNER, YELLOW_BANNER,
					LIME_BANNER, PINK_BANNER, GRAY_BANNER, LIGHT_GRAY_BANNER, CYAN_BANNER, PURPLE_BANNER, BLUE_BANNER,
					BROWN_BANNER, GREEN_BANNER, RED_BANNER, BLACK_BANNER })
				put(map, item, item);
			RECIPES.put(WATER_BUCKET, map);
			// 水桶可被绿色水之力替代，所以绿色水之力拥有水桶全部配方
			put(map, BUCKET, WATER_BUCKET);
			RECIPES.put(GREEN_FORCE_OF_WATER, map);

			map = new HashMap<>();
			put(map, DIRT, GRASS_BLOCK);
			RECIPES.put(GRASS_BLOCK, map);

			map = new HashMap<>();
			put(map, DIRT, MYCELIUM);
			RECIPES.put(MYCELIUM, map);

			RECIPES.put(BONE_MEAL, marker);

			RECIPES.put(CppItems.COMPRESSOR, marker);
		}
		{
			Set<BlockItem> set = new HashSet<>();
			for (Entry<RegistryKey<Item>, Item> entry : Registry.ITEM.getEntries())
				if (entry.getKey().getValue().getPath().contains("_ore"))
					if (entry.getValue() instanceof BlockItem)
						set.add((BlockItem) entry.getValue());
			set.add((BlockItem) ANCIENT_DEBRIS);
			ORES = Collections.unmodifiableSet(set);
		}
	}

	public static class ItemStackAndCount {
		public final ItemStack itemStack;
		public final int count;

		public ItemStackAndCount(ItemStack itemStack) {
			this(itemStack, 1);
		}

		public ItemStackAndCount(ItemStack itemStack, int count) {
			this.itemStack = itemStack;
			this.count = count;
		}

//		@Override
//		public boolean equals(Object obj) {
//			if (obj == null)
//				return false;
//			if (obj == this)
//				return true;
//			if (obj instanceof ItemStackAndCount) {
//				ItemStackAndCount o = (ItemStackAndCount) obj;
//				return count <= o.count && itemStack.equals(o.itemStack);
//			}
//			return false;
//		}
//
//		@Override
//		public int hashCode() {
//			return itemStack.hashCode();
//		}
	}
}
