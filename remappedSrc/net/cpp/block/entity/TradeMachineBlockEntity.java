package net.cpp.block.entity;

import static net.cpp.init.CppItems.EMERALD_TRADE_PLUGIN;
import static net.cpp.init.CppItems.GOLD_TRADE_PLUGIN;
import static net.cpp.init.CppItems.MOON_TRADE_PLUGIN;
import static net.minecraft.item.Items.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.ImmutableSet;
import com.ibm.icu.impl.Pair;

import net.cpp.gui.handler.TradeMachineScreenHandler;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;

public class TradeMachineBlockEntity extends AExpMachineBlockEntity {
	public static final Set<Item> PLUGIN = ImmutableSet.of(EMERALD_TRADE_PLUGIN, GOLD_TRADE_PLUGIN, MOON_TRADE_PLUGIN);
	public static final Set<Item> CURRENCY = ImmutableSet.of(EMERALD, GOLD_INGOT, CppItems.MOON_SHARD);
	public static final Set<Item> PLACE_TAKERS = ImmutableSet.of(FILLED_MAP, CppItems.CHARACTER, TIPPED_ARROW);
	private static final int[] AVAILABLE_SLOTS_1 = new int[] { 0, 1 };
	private static final int[] AVAILABLE_SLOTS_2 = new int[] { 0, 2, 3 };
	/**
	 * 模式1配方 键为被交易机购买的物品，值为Pair(价值点,冷却时间)
	 */
	public static final Map<Item, Pair<Integer, Integer>> BUY_MAP = new HashMap<>();
	/**
	 * 模式2配方 键为交易配方代号，存在NBT的"code"里
	 */
	public static final List<Recipe> SELL_TABLE = new ArrayList<>();
	public static final List<ItemStack> PLUGINS = new ArrayList<>();
	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
	private int tradeValue, emeraldCount, cooldown,
			/**
			 * 0：售卖模式，1：购买模式
			 */
			mode;
	public final PropertyDelegate propertyDelegate = new ExpPropertyDelegate() {

		@Override
		public int size() {
			return 8;
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
			case 4:
				tradeValue = value;
				break;
			case 5:
				emeraldCount = value;
				break;
			case 6:
				cooldown = value;
				break;
			case 7:
				mode = value;
			default:
				super.set(index, value);
			}
		}

		@Override
		public int get(int index) {
			switch (index) {
			case 4:
				return tradeValue;
			case 5:
				return emeraldCount;
			case 6:
				return cooldown;
			case 7:
				return mode;
			default:
				return super.get(index);
			}
		}
	};

	public TradeMachineBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(CppBlockEntities.TRADE_MACHINE, blockPos, blockState);
	}

	@Override
	public void fromTag(CompoundTag tag) {
		cooldown = tag.getInt("cooldown");
		emeraldCount = tag.getInt("emeraldCount");
		mode = tag.getInt("mode");
		tradeValue = tag.getInt("tradeValue");
		super.fromTag(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("cooldown", cooldown);
		tag.putInt("emeraldCount", emeraldCount);
		tag.putInt("mode", mode);
		tag.putInt("tradeValue", tradeValue);
		return super.toTag(tag);
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		if (stack.getItem() == EXPERIENCE_BOTTLE)
			return slot == 0;
		if (mode == 0) {
			return slot == 1 && BUY_MAP.keySet().contains(stack.getItem());
		} else {
			return slot == 2 && PLUGIN.contains(stack.getItem()) || slot == 3 && CURRENCY.contains(stack.getItem());
		}
	}

	@Override
	public PropertyDelegate getPropertyDelegate() {
		return propertyDelegate;
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return mode == 0 ? AVAILABLE_SLOTS_1 : AVAILABLE_SLOTS_2;
	}

	@Override
	public DefaultedList<ItemStack> getInvStackList() {
		return inventory;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		inventory = list;
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new TradeMachineScreenHandler(syncId, playerInventory, this);
	}

	public int getMode() {
		return mode;
	}

	public void shiftMode() {
		mode ^= 1;
	}

	/**
	 * 
	 * @param item       被交易机购买的物品
	 * @param tradeValue 价值点
	 * @param cooldown   冷却时间
	 */
	public static void addBuy(Item item, int tradeValue, int cooldown) {
		BUY_MAP.put(item, Pair.of(tradeValue, cooldown));
	}

	public static void addSell(int code, Item currency, int currencyCount, List<ItemStack> outputs, int experience, int cooldown) {
		SELL_TABLE.add(new Recipe(currency, currencyCount, experience, cooldown, outputs));
	}

	public static void addSell(int code, Item currency, int currencyCount, Item result, int resultCount, int experience, int cooldown) {
		addSell(code, currency, currencyCount, result, resultCount, experience, cooldown, null);
	}

	public static void addSell(int code, Item currency, int currencyCount, ItemStack result, int experience, int cooldown) {
		SELL_TABLE.add(new Recipe(currency, currencyCount, experience, cooldown, result));
	}

	public static void addSell(int code, Item currency, int currencyCount, Item result, int resultCount, int experience, int cooldown, List<ItemStack> outputs) {
		SELL_TABLE.add(code, new Recipe(currency, currencyCount, experience, cooldown, result, resultCount));
	}

	public static void addEnchanted(int code, Item currency, int currencyCount, Item result, int resultCount, int experience, int cooldown) {
		SELL_TABLE.add(new Recipe(currency, currencyCount, experience, cooldown, (blockEntity) -> {
			return EnchantmentHelper.enchant(blockEntity.getWorld().random, new ItemStack(result, resultCount), blockEntity.getWorld().random.nextInt(14) + 5, false);
		}));
	}

	public static List<ItemStack> stacksOf(Item... items) {
		List<ItemStack> rst = new LinkedList<>();
		for (Item item : items) {
			rst.add(item.getDefaultStack());
		}
		return Collections.unmodifiableList(rst);
	}

	public static List<ItemStack> stacksOf(String keyWord) {
		return stacksOf(1, keyWord);
	}

	public static ItemStack enchantOf(Enchantment enchantment) {
		ItemStack itemStack = ENCHANTED_BOOK.getDefaultStack();
		itemStack.addEnchantment(enchantment, enchantment.isTreasure() ? 1 : enchantment.getMaxLevel());
		return itemStack;
	}

	public static List<ItemStack> stacksOf(int count, String keyWord) {
		List<ItemStack> rst = new LinkedList<>();
		for (Entry<RegistryKey<Item>, Item> entry : Registry.ITEM.getEntries())
			if (entry.getKey().getValue().getPath().contains("_dye"))
				rst.add(new ItemStack(entry.getValue(), count));
		return Collections.unmodifiableList(rst);
	}

	public static void tick(World world, BlockPos pos, BlockState state, TradeMachineBlockEntity blockEntity) {
		if (!world.isClient) {
			blockEntity.transferExpBottle();
			if (blockEntity.cooldown > 0)
				blockEntity.cooldown--;
			if (blockEntity.cooldown <= 0) {
				if (blockEntity.mode == 0) {// 交易机向玩家购买
					Pair<Integer, Integer> pair = BUY_MAP.get(blockEntity.getStack(1).getItem());
					if (pair != null) {// 购买并转换成价值点
						blockEntity.tradeValue += pair.first;
						blockEntity.cooldown += pair.second;
						blockEntity.getStack(1).decrement(1);
					}
					if (blockEntity.tradeValue >= 2048) {// 产出绿宝石
						if (blockEntity.expStorage + 7 <= AExpMachineBlockEntity.XP_CAPACITY && blockEntity.tryInsert(2, EMERALD.getDefaultStack())) {
							blockEntity.emeraldCount++;
							blockEntity.expStorage += 7;
							blockEntity.tradeValue -= 2048;
						}
					}
					if (blockEntity.emeraldCount >= 10) {// 产出交易插件
						if (blockEntity.getStack(3).isEmpty()) {
							blockEntity.setStack(3, PLUGINS.get((int) (world.random.nextDouble() * PLUGINS.size())).copy());
							blockEntity.emeraldCount -= 10;
						}
					}
					blockEntity.output(2);
					blockEntity.output(3);
				} else {// 交易机向玩家售卖
					if (PLUGIN.contains(blockEntity.getStack(2).getItem()) && PLACE_TAKERS.contains(blockEntity.getStack(1).getItem())) {
						int code = blockEntity.getStack(2).getOrCreateTag().getInt("code");
						if (blockEntity.getStack(1).isEmpty()) {
							Recipe recipe = SELL_TABLE.get(code);
							if (recipe != null) {
								if (blockEntity.getStack(3).getItem() == recipe.currency && blockEntity.getStack(3).getCount() >= recipe.currencyCount && blockEntity.expStorage >= recipe.experience) {
									if (blockEntity.tryInsert(1, recipe.output(blockEntity).copy())) {
										blockEntity.getStack(3).decrement(recipe.currencyCount);
										blockEntity.cooldown += recipe.cooldown;
										blockEntity.expStorage -= recipe.experience;
									}
								}
							}
						}
					}
					blockEntity.output(1);
				}
			}
		}
	}

	static {
		addBuy(LAVA_BUCKET, 2048, 43);
		addBuy(DIAMOND, 2048, 43);
		addBuy(COMPASS, 2048, 43);
		addBuy(OAK_BOAT, 2048, 43);
		addBuy(SPRUCE_BOAT, 2048, 43);
		addBuy(JUNGLE_BOAT, 2048, 43);
		addBuy(ACACIA_BOAT, 2048, 43);
		addBuy(DARK_OAK_BOAT, 2048, 43);
		addBuy(WRITABLE_BOOK, 2048, 43);
		addBuy(RABBIT_FOOT, 1024, 21);
		addBuy(GOLD_INGOT, 649, 14);
		addBuy(RABBIT, 512, 8);
		addBuy(IRON_INGOT, 512, 11);
		addBuy(SCUTE, 512, 11);
		addBuy(MELON, 512, 11);
		addBuy(PUFFERFISH, 512, 11);
		addBuy(BOOK, 512, 11);
		addBuy(INK_SAC, 410, 9);
		addBuy(PUMPKIN, 341, 7);
		addBuy(TROPICAL_FISH, 341, 7);
		addBuy(LEATHER, 341, 5);
		addBuy(PORKCHOP, 293, 5);
		addBuy(MUTTON, 293, 5);
		addBuy(TRIPWIRE_HOOK, 256, 5);
		addBuy(SPIDER_EYE, 256, 2);
		addBuy(GLASS_BOTTLE, 228, 5);
		addBuy(RABBIT_HIDE, 228, 5);
		addBuy(BEEF, 205, 3);
		addBuy(DRIED_KELP_BLOCK, 205, 4);
		addBuy(SWEET_BERRIES, 205, 4);
		addBuy(COAL, 205, 3);
		addBuy(CLAY_BALL, 205, 3);
		addBuy(GLASS_PANE, 186, 3);
		addBuy(QUARTZ, 171, 4);
		addBuy(RED_DYE, 171, 3);
		addBuy(GREEN_DYE, 171, 3);
		addBuy(PURPLE_DYE, 171, 3);
		addBuy(CYAN_DYE, 171, 3);
		addBuy(LIGHT_GRAY_DYE, 171, 3);
		addBuy(GRAY_DYE, 171, 3);
		addBuy(PINK_DYE, 171, 3);
		addBuy(LIME_DYE, 171, 3);
		addBuy(YELLOW_DYE, 171, 3);
		addBuy(LIGHT_BLUE_DYE, 171, 3);
		addBuy(MAGENTA_DYE, 171, 3);
		addBuy(ORANGE_DYE, 171, 3);
		addBuy(BLACK_DYE, 171, 3);
		addBuy(BROWN_DYE, 171, 3);
		addBuy(BLUE_DYE, 171, 3);
		addBuy(WHITE_DYE, 171, 3);
		addBuy(SALMON, 158, 2);
		addBuy(CHICKEN, 146, 2);
		addBuy(STRING, 146, 2);
		addBuy(ENDER_PEARL, 146, 2);
		addBuy(BEETROOT, 137, 2);
		addBuy(COD, 137, 2);
		addBuy(CHORUS_FRUIT, 137, 2);
		addBuy(GRANITE, 128, 2);
		addBuy(ANDESITE, 128, 2);
		addBuy(DIORITE, 128, 2);
		addBuy(REDSTONE, 128, 2);
		addBuy(WHITE_WOOL, 114, 2);
		addBuy(GRAY_WOOL, 114, 2);
		addBuy(BROWN_WOOL, 114, 2);
		addBuy(BLACK_WOOL, 114, 2);
		addBuy(WHEAT, 102, 2);
		addBuy(STONE, 102, 2);
		addBuy(NETHER_WART, 93, 2);
		addBuy(CARROT, 93, 1);
		addBuy(PAPER, 85, 1);
		addBuy(FEATHER, 85, 1);
		addBuy(POTATO, 79, 1);
		addBuy(FLINT, 79, 4);
		addBuy(ROTTEN_FLESH, 64, 1);
		addBuy(STICK, 64, 1);
		addBuy(OAK_LOG, 57, 1);
		addBuy(SPRUCE_LOG, 57, 1);
		addBuy(BIRCH_LOG, 57, 1);
		addBuy(JUNGLE_LOG, 57, 1);
		addBuy(ACACIA_LOG, 57, 1);
		addBuy(DARK_OAK_LOG, 57, 1);
		addBuy(BONE_MEAL, 57, 1);
		addBuy(GUNPOWDER, 57, 1);
		addBuy(BLAZE_POWDER, 57, 1);
		addBuy(SLIME_BALL, 57, 1);
		addBuy(ARROW, 57, 1);
		addBuy(PRISMARINE_SHARD, 57, 1);

		SELL_TABLE.add(null);
		addSell(1, EMERALD, 1, WHITE_WOOL, 1, 1, 30);
		addSell(2, EMERALD, 1, PACKED_ICE, 1, 1, 30);
		addSell(3, EMERALD, 1, BLUE_ICE, 1, 1, 30);
		addSell(4, EMERALD, 1, stacksOf(BRAIN_CORAL_BLOCK, BUBBLE_CORAL_BLOCK, FIRE_CORAL_BLOCK, HORN_CORAL_BLOCK, TUBE_CORAL_BLOCK), 1, 30);
		addSell(5, EMERALD, 1, LANTERN, 1, 1, 30);
		addSell(6, EMERALD, 1, CAKE, 1, 1, 30);
		addSell(7, EMERALD, 1, CppItems.RABBIT_STEW, 1, 1, 30);

		List<ItemStack> tmpList = new LinkedList<>();
		ItemStack stew = SUSPICIOUS_STEW.getDefaultStack();
		SuspiciousStewItem.addEffectToStew(stew, StatusEffects.SATURATION, 140);
		tmpList.add(stew);
		stew = SUSPICIOUS_STEW.getDefaultStack();
		SuspiciousStewItem.addEffectToStew(stew, StatusEffects.BLINDNESS, 120);
		tmpList.add(stew);
		stew = SUSPICIOUS_STEW.getDefaultStack();
		SuspiciousStewItem.addEffectToStew(stew, StatusEffects.JUMP_BOOST, 160);
		tmpList.add(stew);
		stew = SUSPICIOUS_STEW.getDefaultStack();
		SuspiciousStewItem.addEffectToStew(stew, StatusEffects.WEAKNESS, 140);
		tmpList.add(stew);
		stew = SUSPICIOUS_STEW.getDefaultStack();
		SuspiciousStewItem.addEffectToStew(stew, StatusEffects.NIGHT_VISION, 100);
		tmpList.add(stew);
		stew = SUSPICIOUS_STEW.getDefaultStack();
		SuspiciousStewItem.addEffectToStew(stew, StatusEffects.POISON, 280);
		tmpList.add(stew);
		addSell(8, EMERALD, 1, tmpList, 1, 30);

		addSell(9, EMERALD, 1, TERRACOTTA, 1, 1, 30);
		addSell(10, EMERALD, 1, LAPIS_LAZULI, 1, 1, 30);
		addSell(11, EMERALD, 1, GUNPOWDER, 1, 1, 30);
		addSell(12, EMERALD, 1, GOLDEN_CARROT, 1, 1, 30);
		addSell(13, EMERALD, 1, GLISTERING_MELON_SLICE, 1, 1, 30);
		addSell(14, EMERALD, 1, COD_BUCKET, 1, 1, 30);
		addSell(15, EMERALD, 1, SALMON_BUCKET, 1, 1, 30);
		addSell(16, EMERALD, 1, PUFFERFISH_BUCKET, 1, 1, 30);
		addSell(17, EMERALD, 1, TROPICAL_FISH_BUCKET, 1, 1, 30);
		addSell(18, EMERALD, 1, REDSTONE, 2, 1, 30);

		tmpList = new LinkedList<ItemStack>();
		for (Entry<RegistryKey<Potion>, Potion> entry : Registry.POTION.getEntries()) {
			ItemStack itemStack = new ItemStack(TIPPED_ARROW, 2);
			PotionUtil.setPotion(itemStack, entry.getValue());
			tmpList.add(itemStack);
		}
		addSell(19, EMERALD, 1, tmpList, 1, 30);

		addSell(20, EMERALD, 1, APPLE, 4, 1, 30);
		addSell(21, EMERALD, 1, PUMPKIN_PIE, 4, 1, 30);
		addSell(22, EMERALD, 1, GLASS, 4, 1, 30);
		addSell(23, EMERALD, 1, RED_SAND, 4, 1, 30);
		addSell(24, EMERALD, 1, stacksOf(4, "_dye"), 1, 30);
		addSell(25, EMERALD, 1, COOKED_PORKCHOP, 5, 1, 30);
		addSell(26, EMERALD, 1, BREAD, 6, 1, 30);
		addSell(27, EMERALD, 1, COOKIE, 12, 1, 30);
		addSell(28, EMERALD, 1, COOKED_CHICKEN, 8, 1, 30);
		addSell(29, EMERALD, 1, SAND, 8, 1, 30);
		addSell(30, EMERALD, 1, BRICK, 10, 1, 30);
		addSell(31, EMERALD, 1, ARROW, 16, 1, 30);
		addSell(32, EMERALD, 2, QUARTZ_BLOCK, 1, 1, 300);
		addSell(33, EMERALD, 2, COPPER_BLOCK, 1, 1, 300);
		addSell(34, EMERALD, 2, GLOWSTONE, 1, 1, 300);
		addSell(35, EMERALD, 2, NAUTILUS_SHELL, 1, 1, 300);
		addSell(36, EMERALD, 5, ENDER_PEARL, 1, 1, 120);
		addSell(37, EMERALD, 5, SLIME_BALL, 1, 1, 120);
		addSell(38, EMERALD, 6, QUARTZ_BLOCK, 1, 1, 100);
		addSell(39, EMERALD, 6, MAP, 1, 1, 100);
		addSell(40, EMERALD, 6, ITEM_FRAME, 1, 1, 100);
		addSell(41, EMERALD, 10, GLOBE_BANNER_PATTERN, 1, 1, 60);
		addSell(42, EMERALD, 10, BOOKSHELF, 1, 1, 60);
		addSell(43, EMERALD, 20, NAME_TAG, 1, 1, 40);
		addSell(44, EMERALD, 30, BELL, 1, 1, 40);
		addEnchanted(45, EMERALD, 20, DIAMOND_SWORD, 1, 1, 40);
		addEnchanted(46, EMERALD, 24, DIAMOND_AXE, 1, 1, 40);
		addEnchanted(47, EMERALD, 25, DIAMOND_PICKAXE, 1, 1, 40);
		addEnchanted(48, EMERALD, 17, DIAMOND_SHOVEL, 1, 1, 40);
		addEnchanted(49, EMERALD, 14, DIAMOND_HOE, 1, 1, 40);
		addEnchanted(50, EMERALD, 20, DIAMOND_HELMET, 1, 1, 40);
		addEnchanted(51, EMERALD, 28, DIAMOND_CHESTPLATE, 1, 1, 40);
		addEnchanted(52, EMERALD, 26, DIAMOND_LEGGINGS, 1, 1, 40);
		addEnchanted(53, EMERALD, 20, DIAMOND_BOOTS, 1, 1, 40);
		addEnchanted(54, EMERALD, 12, FISHING_ROD, 1, 1, 40);
		addEnchanted(55, EMERALD, 14, BOW, 1, 1, 40);
		addEnchanted(56, EMERALD, 15, CROSSBOW, 1, 1, 40);

		addSell(57, EMERALD, 36, enchantOf(Enchantments.PROTECTION), 1, 40);
		addSell(58, EMERALD, 36, enchantOf(Enchantments.FIRE_PROTECTION), 1, 40);
		addSell(59, EMERALD, 36, enchantOf(Enchantments.FEATHER_FALLING), 1, 40);
		addSell(60, EMERALD, 36, enchantOf(Enchantments.BLAST_PROTECTION), 1, 40);
		addSell(61, EMERALD, 36, enchantOf(Enchantments.PROJECTILE_PROTECTION), 1, 40);
		addSell(62, EMERALD, 28, enchantOf(Enchantments.RESPIRATION), 1, 40);
		addSell(63, EMERALD, 12, enchantOf(Enchantments.AQUA_AFFINITY), 1, 40);
		addSell(64, EMERALD, 28, enchantOf(Enchantments.THORNS), 1, 40);
		addSell(65, EMERALD, 28, enchantOf(Enchantments.DEPTH_STRIDER), 1, 40);
		addSell(66, EMERALD, 20, enchantOf(Enchantments.FROST_WALKER), 1, 40);
		addSell(67, EMERALD, 44, enchantOf(Enchantments.SHARPNESS), 1, 40);
		addSell(68, EMERALD, 44, enchantOf(Enchantments.SMITE), 1, 40);
		addSell(69, EMERALD, 44, enchantOf(Enchantments.BANE_OF_ARTHROPODS), 1, 40);
		addSell(70, EMERALD, 20, enchantOf(Enchantments.KNOCKBACK), 1, 40);
		addSell(71, EMERALD, 20, enchantOf(Enchantments.FIRE_ASPECT), 1, 40);
		addSell(72, EMERALD, 28, enchantOf(Enchantments.LOOTING), 1, 40);
		addSell(73, EMERALD, 28, enchantOf(Enchantments.SWEEPING), 1, 40);
		addSell(74, EMERALD, 44, enchantOf(Enchantments.EFFICIENCY), 1, 40);
		addSell(75, EMERALD, 12, enchantOf(Enchantments.SILK_TOUCH), 1, 40);
		addSell(76, EMERALD, 28, enchantOf(Enchantments.UNBREAKING), 1, 40);
		addSell(77, EMERALD, 28, enchantOf(Enchantments.FORTUNE), 1, 40);
		addSell(78, EMERALD, 44, enchantOf(Enchantments.POWER), 1, 40);
		addSell(79, EMERALD, 20, enchantOf(Enchantments.PUNCH), 1, 40);
		addSell(80, EMERALD, 12, enchantOf(Enchantments.FLAME), 1, 40);
		addSell(81, EMERALD, 12, enchantOf(Enchantments.INFINITY), 1, 40);
		addSell(82, EMERALD, 28, enchantOf(Enchantments.LUCK_OF_THE_SEA), 1, 40);
		addSell(83, EMERALD, 28, enchantOf(Enchantments.LURE), 1, 40);
		addSell(84, EMERALD, 28, enchantOf(Enchantments.LOYALTY), 1, 40);
		addSell(85, EMERALD, 44, enchantOf(Enchantments.IMPALING), 1, 40);
		addSell(86, EMERALD, 28, enchantOf(Enchantments.RIPTIDE), 1, 40);
		addSell(87, EMERALD, 12, enchantOf(Enchantments.CHANNELING), 1, 40);
		addSell(88, EMERALD, 24, enchantOf(Enchantments.MENDING), 1, 40);
		addSell(89, EMERALD, 24, enchantOf(Enchantments.BINDING_CURSE), 1, 40);
		addSell(90, EMERALD, 24, enchantOf(Enchantments.VANISHING_CURSE), 1, 40);
		addSell(91, EMERALD, 36, enchantOf(Enchantments.PIERCING), 1, 40);
		addSell(92, EMERALD, 28, enchantOf(Enchantments.QUICK_CHARGE), 1, 40);
		addSell(93, EMERALD, 12, enchantOf(Enchantments.MULTISHOT), 1, 40);

		SELL_TABLE.add(new Recipe(GOLD_INGOT, 1, 4, 120, LootTables.PIGLIN_BARTERING_GAMEPLAY));
		addSell(95, CppItems.MOON_SHARD, 1, CppItems.CHARACTER, 1, 64, 40);
		addSell(96, CppItems.MOON_SHARD, 1, SHULKER_BOX, 1, 64, 40);
		addSell(97, CppItems.MOON_SHARD, 1, CppItems.SANTA_GIFT, 1, 64, 40);
		SELL_TABLE.add(new Recipe(CppItems.MOON_SHARD, 1, 64, 40, blockEntity -> {
			try {
				ServerWorld serverWorld = (ServerWorld) blockEntity.getWorld();
				BiomeSource biomeSource = serverWorld.getChunkManager().getChunkGenerator().getBiomeSource();
				List<StructureFeature<?>> structureFeatures = new ArrayList<>();
				for (Entry<RegistryKey<StructureFeature<?>>, StructureFeature<?>> entry : Registry.STRUCTURE_FEATURE.getEntries()) {
					if (biomeSource.hasStructureFeature(entry.getValue()))
						structureFeatures.add(entry.getValue());
				}
				StructureFeature<?> structureFeature = structureFeatures.get((int) (serverWorld.random.nextDouble() * structureFeatures.size()));
				BlockPos blockPos = serverWorld.locateStructure(structureFeature, blockEntity.getPos(), 50, true);
				if (blockPos != null) {
					ItemStack itemStack = FilledMapItem.createMap(serverWorld, blockPos.getX(), blockPos.getZ(), (byte) 1, true, true);
					FilledMapItem.fillExplorationMap(serverWorld, itemStack);
					MapState.addDecorationsTag(itemStack, blockPos, "X", MapIcon.Type.RED_X);
					itemStack.setCustomName(new TranslatableText("structure_feature." + structureFeature.getName()).append(new TranslatableText("item.explorer_map")));
					return itemStack;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ItemStack.EMPTY;
		}));

		PLUGINS.add(ItemStack.EMPTY);
		for (Recipe recipe : SELL_TABLE) {
			if (recipe == null)
				continue;
			ItemStack itemStack = ItemStack.EMPTY;
			if (recipe.currency == EMERALD) {
				itemStack = CppItems.EMERALD_TRADE_PLUGIN.getDefaultStack();
			} else if (recipe.currency == GOLD_INGOT) {
				itemStack = CppItems.GOLD_TRADE_PLUGIN.getDefaultStack();
			} else if (recipe.currency == CppItems.MOON_SHARD) {
				itemStack = CppItems.MOON_TRADE_PLUGIN.getDefaultStack();
			}
			itemStack.getOrCreateTag().putInt("code", PLUGINS.size());
			PLUGINS.add(itemStack);
		}
	}

	public static class Recipe implements Function<BlockEntity, ItemStack> {
		public final Item currency;
		public final int currencyCount, experience, cooldown;
		public final Function<BlockEntity, ItemStack> outputer;

		public Recipe(Item currency, int currencyCount, int experience, int cooldown, Item item) {
			this(currency, currencyCount, experience, cooldown, (blockEntity) -> new ItemStack(item, 1));
		}

		public Recipe(Item currency, int currencyCount, int experience, int cooldown, Item item, int count) {
			this(currency, currencyCount, experience, cooldown, (blockEntity) -> new ItemStack(item, count));
		}

		public Recipe(Item currency, int currencyCount, int experience, int cooldown, ItemStack result) {
			this(currency, currencyCount, experience, cooldown, (blockEntity) -> result);
		}

		public Recipe(Item currency, int currencyCount, int experience, int cooldown, Identifier lootTable) {
			this(currency, currencyCount, experience, cooldown, (blockEntity) -> {
				return blockEntity.getWorld().getServer().getLootManager().getTable(lootTable).generateLoot((new LootContext.Builder((ServerWorld) blockEntity.getWorld())).random(blockEntity.getWorld().random).build(LootContextTypes.EMPTY)).get(0);
			});
		}

		public Recipe(Item currency, int currencyCount, int experience, int cooldown, List<ItemStack> results) {
			this(currency, currencyCount, experience, cooldown, (blockEntity) -> results.get((int) (results.size() * blockEntity.getWorld().random.nextDouble())));
		}

		public Recipe(Item currency, int currencyCount, int experience, int cooldown, Function<BlockEntity, ItemStack> outputer) {
			this.currency = currency;
			this.currencyCount = currencyCount;
			this.experience = experience;
			this.cooldown = cooldown;
			this.outputer = outputer;
		}

		public ItemStack output(BlockEntity blockEntity) {
			return apply(blockEntity);
		}

		@Override
		public ItemStack apply(BlockEntity blockEntity) {
			return outputer.apply(blockEntity);
		}
	}
}
