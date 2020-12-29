package net.cpp.block.entity;

import static net.cpp.init.CppBlocks.RARE_EARTH_GLASS;
import static net.cpp.init.CppBlocks.REINFORCED_GLASS;
import static net.cpp.init.CppItems.*;
import static net.minecraft.item.Items.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.cpp.block.AllInOneMachineBlock;
import net.cpp.gui.handler.AllInOneMachineScreenHandler;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
/**
 * 多功能一体机
 * @author Ph-苯
 *
 */
public class AllInOneMachineBlockEntity extends AExpMachineBlockEntity {
	/**
	 * 不被消耗的原料
	 */
	public static final Set<Item> UNCONSUMABLE = new HashSet<>(Arrays.asList(LAVA_BUCKET, COBBLESTONE_PLUGIN, STONE_PLUGIN, BLACKSTONE_PLUGIN, NETHERRACK_PLUGIN, END_STONE_PLUGIN, BASALT_PLUGIN, GREEN_FORCE_OF_WATER));
	private static final int[] AVAILABLE_SLOTS = new int[] { 0, 1, 2 };
	private static final Map<Integer, Recipe> RECIPES = new HashMap<>();
	private static final Map<Integer, List<Recipe>> RANDOM_RECIPES = new HashMap<>();
	private int lastTickRecipeCode;
	private ItemStack[] lastTickOutputs;
	private Degree temperature = Degree.ORDINARY;
	private Degree pressure = Degree.ORDINARY;
	/**
	 * 可用的温度
	 */
	private Set<Degree> availabeTemperature = EnumSet.of(Degree.ORDINARY);
	/**
	 * 可用的压强
	 */
	private Set<Degree> availabePressure = EnumSet.of(Degree.ORDINARY);
	public final PropertyDelegate propertyDelegate = new ExpPropertyDelegate() {

		@Override
		public int size() {
			return 5;
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
			case 4:
				setTemperature(Degree.values()[value / 16 % Degree.values().length]);
				setPressure(Degree.values()[value % 16 % Degree.values().length]);
				break;
			default:
				super.set(index, value);
			}
		}

		@Override
		public int get(int index) {
			switch (index) {
			case 4:
				return temperature.ordinal() * 16 + pressure.ordinal();
			default:
				return super.get(index);
			}
		}
	};

	public AllInOneMachineBlockEntity() {
		this(BlockPos.ORIGIN, CppBlocks.ALL_IN_ONE_MACHINE.getDefaultState());
	}

	public AllInOneMachineBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(CppBlockEntities.ALL_IN_ONE_MACHINE, blockPos, blockState);
		setCapacity(5);
	}

	@Override
	public PropertyDelegate getPropertyDelegate() {
		return propertyDelegate;
	}

	/*
	 * 以下是LootableContainerBlockEntity的方法
	 */
	@Override
	public DefaultedList<ItemStack> getInvStackList() {
		return inventory;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		inventory = list;
	}

	/*
	 * 以下是LockableContainerBlockEntity的方法
	 */
	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		Inventories.fromTag(tag, inventory);
		propertyDelegate.set(4, tag.getInt("temperaturePressure"));
		for (int i = 0, a = tag.getInt("availabeTemperature"); a > 0 && i < Degree.values().length; i++) {
			if ((a & 1) == 1)
				availabeTemperature.add(Degree.values()[i]);
			a >>= 1;
		}
		for (int i = 0, a = tag.getInt("availabePressure"); a > 0 && i < Degree.values().length; i++) {
			if ((a & 1) == 1)
				availabePressure.add(Degree.values()[i]);
			a >>= 1;
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		Inventories.toTag(tag, inventory);
		tag.putInt("temperaturePressure", propertyDelegate.get(4));
		int a = 0;
		for (Degree degree : availabeTemperature) {
			a |= 1 << degree.ordinal();
		}
		tag.putInt("availabeTemperature", a);
		a = 0;
		for (Degree degree : availabePressure) {
			a |= 1 << degree.ordinal();
		}
		tag.putInt("availabePressure", a);
		return tag;
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new AllInOneMachineScreenHandler(syncId, playerInventory, this);
	}

	public static void tick(World world, BlockPos pos, BlockState state, AllInOneMachineBlockEntity blockEntity) {
		if (!world.isClient) {
			blockEntity.transferExpBottle();
			boolean reciped = false;// 有配方吗？
			if (!blockEntity.getStack(1 + 0).isEmpty() && !blockEntity.getStack(1 + 1).isEmpty()) {
				int code = getHashCode(blockEntity.temperature, blockEntity.pressure, blockEntity.getStack(1 + 0).getItem(), blockEntity.getStack(1 + 1).getItem());
				Recipe recipe = RECIPES.get(code);
				if (recipe != null) {

					// 防止哈希码出错，再检查一遍
					if ((recipe.temperature == blockEntity.temperature && recipe.pressure == blockEntity.pressure)

							// 无序配方的检查
							&& (recipe.input1 == blockEntity.getStack(1).getItem() && recipe.input2 == blockEntity.getStack(2).getItem() || recipe.input1 == blockEntity.getStack(2).getItem() && recipe.input2 == blockEntity.getStack(1).getItem())

							// 酿造配方需要检测药水的NBT
							&& (blockEntity.temperature != Degree.ORDINARY || blockEntity.pressure != Degree.LOW || blockEntity.getStack(1).getTag() != null && "minecraft:awkward".equals(blockEntity.getStack(1).getTag().getString("Potion")) || blockEntity.getStack(2).getTag() != null && "minecraft:awkward".equals(blockEntity.getStack(2).getTag().getString("Potion")))) {
						reciped = true;// 确定有配方了
						blockEntity.workTimeTotal = recipe.time;
						if (blockEntity.expStorage >= recipe.experience) {
							if (RANDOM_RECIPES.containsKey(code)) {// 若为随机配方，则从配方列表中随机抽取一个配方
								List<Recipe> list = RANDOM_RECIPES.get(code);
								recipe = list.get((int) (Math.random() * list.size()));
							}

							// 如果上次的配方代号与这次相同，则不重新获取产物，
							// 防止有随机数量的输出的配方因为阻塞而不断刷新随机数量
							ItemStack[] outputs;
							if (blockEntity.lastTickRecipeCode != code) {
								blockEntity.lastTickOutputs = recipe.output();
								blockEntity.lastTickRecipeCode = code;
							}
							outputs = blockEntity.lastTickOutputs;

							if (blockEntity.canInsert(3, outputs[0]) && blockEntity.canInsert(4, outputs[1])) {
								if (blockEntity.workTime >= blockEntity.workTimeTotal) {
									for (int i = 0; i < 2; i++) {
										if (!UNCONSUMABLE.contains(blockEntity.getStack(i + 1).getItem())) {// 有些物品不消耗
											blockEntity.getStack(i + 1).decrement(1);
										}
										blockEntity.insert(i + 3, outputs[i]);
									}
									blockEntity.expStorage -= recipe.experience;
									blockEntity.workTime = 0;

									// 成功产出后刷新成品，防止有随机数量的配方每次的成品相同
									blockEntity.lastTickOutputs = recipe.output();
								} else {
									blockEntity.workTime++;
								}
							}
						}

					}
				}
			}
			if (!reciped) {// 如果没有配方，数据清零
				blockEntity.workTime = 0;
				blockEntity.workTimeTotal = 0;
				blockEntity.lastTickRecipeCode = 0;
				blockEntity.lastTickOutputs = null;
			}
//			for (int i = 0; i < 2; i++) {// 输出成品栏的物品
//				if (!blockEntity.getStack(i + 3).isEmpty()) {
//					blockEntity.setStack(i + 3, blockEntity.output(blockEntity.getStack(i + 3)));
//				}
//			}
			// 输出成品栏的物品
			blockEntity.output(3);
			blockEntity.output(4);
			world.setBlockState(pos, blockEntity.getCachedState().with(AllInOneMachineBlock.WORKING, blockEntity.isWorking()));// 更新方块状态
		}
	}

	/*
	 * 以下是Inventory的方法
	 */

	@Override
	public void onOpen(PlayerEntity player) {
//		propertyDelegate.set(3, propertyDelegate.get(3));
		super.onOpen(player);
	}

	/*
	 * 以下是SidedInventory的方法
	 */

	@Override
	public int[] getAvailableSlots(Direction side) {
		return AVAILABLE_SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		if (stack.getItem() == EXPERIENCE_BOTTLE)
			return slot == 0;
		return (slot == 1 && stack.getItem() != getStack(2).getItem()) || (slot == 2 && stack.getItem() != getStack(1).getItem());
	}

	/*
	 * 以下是自定义方法
	 */
/**
 * 获取当前温度
 * @return
 */
	public Degree getTemperature() {
		return temperature;
	}
/**
 * 设置温度
 * @param t 温度
 * @return
 */
	public boolean setTemperature(Degree t) {
		boolean set = false;
		if (availabeTemperature.contains(t)) {
			temperature = t;
			set = true;
		}
		return set;
	}
/**
 * 切换温度
 */
	public void shiftTemperature() {
		int i = temperature.ordinal();
		while (!setTemperature(Degree.values()[++i % Degree.values().length]))
			;
	}
/**
 * 添加可用温度
 * @param degree
 * @return
 */
	public boolean addAvailableTemperature(Degree degree) {
		return availabeTemperature.add(degree);
	}
/**
 * 获取当前压强
 * @return
 */
	public Degree getPressure() {
		return pressure;
	}
/**
 * 设置压强
 * @param p 压强
 * @return
 */
	public boolean setPressure(Degree p) {
		boolean set = false;
		if (availabePressure.contains(p)) {
			pressure = p;
			set = true;
		}
		return set;
	}
	/**
	 * 切换压强
	 */
	public void shiftPressure() {
		int i = pressure.ordinal();
		while (!setPressure(Degree.values()[++i % Degree.values().length]))
			;
	}
	/**
	 * 添加可用压强
	 * @param degree 压强
	 * @return
	 */
	public boolean addAvailablePressure(Degree degree) {
		return availabePressure.add(degree);
	}
/**
 * 根据原料和温度压强计算哈希码，便于查找配方
 * @param temperature 温度
 * @param pressure 压强
 * @param input1 原料1
 * @param input2 原料2
 * @return 哈希码
 */
	public static int getHashCode(Degree temperature, Degree pressure, Item input1, Item input2) {
		return (input1.hashCode() ^ input2.hashCode()) * 256 + temperature.ordinal() * 16 + pressure.ordinal();
	}

	private static void addRecipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, ItemStack output2, int experience, int time) {
		addRecipe(temperature, pressure, input1, input2, output1, output2, output2.getCount(), output2.getCount(), experience, time);
	}

	private static void addRecipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, int experience, int time) {
		addRecipe(temperature, pressure, input1, input2, output1, ItemStack.EMPTY, 0, experience, time);
	}

	private static void addRecipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, ItemStack output2, float count2, int experience, int time) {
		addRecipe(temperature, pressure, input1, input2, output1, output2, count2, count2 + 1, experience, time);
	}

	private static void addRecipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, ItemStack output2, float count2Min, float count2Max, int experience, int time) {
		addRecipe(temperature, pressure, input1, input2, output1, output2, output1.getCount(), output1.getCount(), count2Min, count2Max, experience, time);
	}

	private static void addRecipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, ItemStack output2, float count1Min, float count1Max, float count2Min, float count2Max, int experience, int time) {
		RECIPES.put(getHashCode(temperature, pressure, input1, input2), new Recipe(temperature, pressure, input1, input2, output1, output2, count1Min, count1Max, count2Min, count2Max, experience, time));
	}

	static {
		Map<Item, ItemStack> oreRateMap = new HashMap<>();
		oreRateMap.put(COAL_ORE, new ItemStack(COAL));
		oreRateMap.put(COPPER_ORE, new ItemStack(COPPER_INGOT));
		oreRateMap.put(DIAMOND_ORE, new ItemStack(DIAMOND));
		oreRateMap.put(EMERALD_ORE, new ItemStack(EMERALD));
		oreRateMap.put(GOLD_ORE, new ItemStack(GOLD_INGOT));
		oreRateMap.put(IRON_ORE, new ItemStack(IRON_INGOT));
		oreRateMap.put(LAPIS_ORE, new ItemStack(LAPIS_LAZULI, 6));
		oreRateMap.put(NETHER_GOLD_ORE, new ItemStack(GOLD_INGOT));
		oreRateMap.put(NETHER_QUARTZ_ORE, new ItemStack(QUARTZ));
		oreRateMap.put(REDSTONE_ORE, new ItemStack(REDSTONE, 5));
		oreRateMap.put(ANCIENT_DEBRIS, new ItemStack(NETHERITE_SCRAP));
		/*
		 * 高温高压
		 */
		int rand = new Random(new File(".").getAbsolutePath().hashCode()).nextInt();
		for (Map.Entry<Item, ItemStack> entry1 : oreRateMap.entrySet()) {
			for (Map.Entry<Item, ItemStack> entry2 : oreRateMap.entrySet()) {
				if (entry1.equals(entry2))
					break;
				float randf = (entry1.getValue().hashCode() ^ entry2.getValue().hashCode() ^ rand >>> 1) % 90 / 30f + 1;
				float c1 = randf * entry1.getValue().getCount();
				float c2 = (5 - randf) * entry2.getValue().getCount();
				addRecipe(Degree.HIGH, Degree.HIGH, entry1.getKey(), entry2.getKey(), entry1.getValue(), entry2.getValue(), c1, c1 + 1, c2, c2, 4, 200);
			}
		}
		/*
		 * 高温常压
		 */
		for (Item sand: new Item[] {SAND,RED_SAND}) {
			addRecipe(Degree.HIGH, Degree.ORDINARY, COPPER_DUST, sand, new ItemStack(COPPER_INGOT), new ItemStack(CINDER), 0.2F, 1, 50);
			addRecipe(Degree.HIGH, Degree.ORDINARY, IRON_DUST, sand, new ItemStack(IRON_INGOT), new ItemStack(CINDER), 0.2F, 1, 50);
			addRecipe(Degree.HIGH, Degree.ORDINARY, GOLD_DUST, sand, new ItemStack(GOLD_INGOT), new ItemStack(CINDER), 0.2F, 1, 50);
			addRecipe(Degree.HIGH, Degree.ORDINARY, CARBON_DUST, sand, new ItemStack(COAL), new ItemStack(CINDER), 0.2F, 1, 50);
			addRecipe(Degree.HIGH, Degree.ORDINARY, DIAMOND_DUST, sand, new ItemStack(DIAMOND), new ItemStack(CINDER), 0.2F, 1, 50);
			addRecipe(Degree.HIGH, Degree.ORDINARY, EMERALD_DUST, sand, new ItemStack(EMERALD), new ItemStack(CINDER), 0.2F, 1, 50);
			addRecipe(Degree.HIGH, Degree.ORDINARY, QUARTZ_DUST, sand, new ItemStack(QUARTZ), new ItemStack(CINDER), 0.2F, 1, 50);
			addRecipe(Degree.HIGH, Degree.ORDINARY, SILICON_DUST, sand, new ItemStack(SILICON_PLATE), new ItemStack(CINDER), 0.2F, 4, 200);
			addRecipe(Degree.HIGH, Degree.ORDINARY, RARE_EARTH_DUST, sand, new ItemStack(RARE_EARTH_GLASS.asItem()), new ItemStack(CINDER), 0.2F, 4, 200);
			addRecipe(Degree.HIGH, Degree.ORDINARY, STEEL_DUST, sand, new ItemStack(REINFORCED_GLASS.asItem()), new ItemStack(CINDER), 0.2F, 4, 200);
			addRecipe(Degree.HIGH, Degree.ORDINARY, GLASS_BOTTLE, sand, new ItemStack(BOTTLE_OF_SALT), 4, 200);
		}
		/*
		 * 高温低压
		 */
		addRecipe(Degree.HIGH, Degree.LOW, ACID, NETHER_WART, new ItemStack(BIONIC_ACID), 4, 200);
		addRecipe(Degree.HIGH, Degree.LOW, SODA_WATER, NETHER_WART, new ItemStack(ALKALOID), 4, 200);
		addRecipe(Degree.HIGH, Degree.LOW, QUARTZ_DUST, FLINT, new ItemStack(COARSE_SILICON), 4, 200);
		addRecipe(Degree.HIGH, Degree.LOW, GLASS_BOTTLE, PISTON, new ItemStack(BOTTLE_OF_AIR), 4, 200);
		addRecipe(Degree.HIGH, Degree.LOW, COARSE_SILICON, BOTTLE_OF_AIR, new ItemStack(SILICON_DUST), 4, 200);
		addRecipe(Degree.HIGH, Degree.LOW, CLAY_BALL, BOTTLE_OF_SALT, new ItemStack(RARE_EARTH_SALT), 4, 200);
		addRecipe(Degree.HIGH, Degree.LOW, RARE_EARTH_SALT, ALKALOID, new ItemStack(ALKALOID_RARE_EARTH), 4, 200);
		addRecipe(Degree.HIGH, Degree.LOW, ALKALOID_RARE_EARTH, BIONIC_ACID, new ItemStack(RARE_EARTH_DUST), 4, 200);
		addRecipe(Degree.HIGH, Degree.LOW, ALKALOID, FERTILIZER, new ItemStack(AMMONIA_REFRIGERANT), new ItemStack(CINDER), 4, 200);
		addRecipe(Degree.HIGH, Degree.LOW, SLIME_BALL, DIRT, new ItemStack(CLAY), 4, 200);
		/*
		 * 常温高压
		 */
		addRecipe(Degree.ORDINARY, Degree.HIGH, ANCIENT_DEBRIS, FLINT, new ItemStack(NETHERITE_SCRAP, 2), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.HIGH, COPPER_ORE, FLINT, new ItemStack(COPPER_DUST, 2), new ItemStack(IRON_DUST), 0.1F, 4, 200);
		addRecipe(Degree.ORDINARY, Degree.HIGH, IRON_ORE, FLINT, new ItemStack(IRON_DUST, 2), new ItemStack(GOLD_DUST), 0.1F, 4, 200);
		addRecipe(Degree.ORDINARY, Degree.HIGH, GOLD_ORE, FLINT, new ItemStack(GOLD_DUST, 2), new ItemStack(IRON_DUST), 0.1F, 4, 200);
		addRecipe(Degree.ORDINARY, Degree.HIGH, NETHER_GOLD_ORE, FLINT, new ItemStack(GOLD_DUST, 2), new ItemStack(IRON_DUST), 0.1F, 4, 200);
		addRecipe(Degree.ORDINARY, Degree.HIGH, COAL_ORE, FLINT, new ItemStack(CARBON_DUST, 2), new ItemStack(DIAMOND_DUST), 0.02F, 4, 200);
		addRecipe(Degree.ORDINARY, Degree.HIGH, EMERALD_ORE, FLINT, new ItemStack(EMERALD_DUST, 2), new ItemStack(DIAMOND_DUST), 0.1F, 4, 200);
		addRecipe(Degree.ORDINARY, Degree.HIGH, DIAMOND_ORE, FLINT, new ItemStack(DIAMOND_DUST, 2), new ItemStack(CARBON_DUST), 0.1F, 4, 200);
		addRecipe(Degree.ORDINARY, Degree.HIGH, NETHER_QUARTZ_ORE, FLINT, new ItemStack(QUARTZ_DUST, 2), new ItemStack(GLOWSTONE_DUST), 0.1F, 4, 200);
		addRecipe(Degree.ORDINARY, Degree.HIGH, REDSTONE_ORE, FLINT, new ItemStack(REDSTONE_BLOCK), new ItemStack(LAPIS_LAZULI), 2F, 5F, 4, 200);
		addRecipe(Degree.ORDINARY, Degree.HIGH, LAPIS_ORE, FLINT, new ItemStack(LAPIS_LAZULI), new ItemStack(REDSTONE), 10F, 21F, 2F, 5F, 4, 200);
		addRecipe(Degree.ORDINARY, Degree.HIGH, COPPER_INGOT, FLINT, new ItemStack(COPPER_DUST), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, IRON_INGOT, FLINT, new ItemStack(IRON_DUST), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, GOLD_INGOT, FLINT, new ItemStack(GOLD_DUST), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, COAL, FLINT, new ItemStack(CARBON_DUST), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, CHARCOAL, FLINT, new ItemStack(CARBON_DUST), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, CINDER, FLINT, new ItemStack(CARBON_DUST), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, EMERALD, FLINT, new ItemStack(EMERALD_DUST), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, DIAMOND, FLINT, new ItemStack(DIAMOND_DUST), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, QUARTZ, FLINT, new ItemStack(QUARTZ_DUST), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, GRANITE, FLINT, new ItemStack(QUARTZ_DUST), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, ANDESITE, FLINT, new ItemStack(QUARTZ_DUST), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, DIORITE, FLINT, new ItemStack(QUARTZ_DUST), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, CLAY, FLINT, new ItemStack(SLIME_BALL), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, PRISMARINE, FLINT, new ItemStack(PRISMARINE_SHARD, 3), new ItemStack(PRISMARINE_CRYSTALS), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, PRISMARINE_BRICKS, FLINT, new ItemStack(PRISMARINE_SHARD, 3), new ItemStack(PRISMARINE_CRYSTALS, 2), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, DARK_PRISMARINE, FLINT, new ItemStack(PRISMARINE_SHARD, 3), new ItemStack(PRISMARINE_CRYSTALS, 2), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, WHITE_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(WHITE_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, ORANGE_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(ORANGE_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, MAGENTA_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(MAGENTA_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, LIGHT_BLUE_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(LIGHT_BLUE_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, YELLOW_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(YELLOW_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, LIME_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(LIME_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, PINK_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(PINK_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, GRAY_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(GRAY_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, LIGHT_GRAY_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(LIGHT_GRAY_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, CYAN_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(CYAN_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, PURPLE_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(PURPLE_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, BLUE_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(BLUE_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, BROWN_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(BROWN_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, GREEN_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(GREEN_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, RED_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(RED_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, BLACK_WOOL, FLINT, new ItemStack(STRING, 4), new ItemStack(BLACK_DYE), 1, 50);
		addRecipe(Degree.ORDINARY, Degree.HIGH, SOUL_SAND, DIRT, new ItemStack(SOUL_SOIL, 2), 1, 20);
		addRecipe(Degree.ORDINARY, Degree.HIGH, COBBLESTONE, FLINT, new ItemStack(SAND), new ItemStack(FLINT), 1, 20);
		/*
		 * 常温常压
		 */
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, WHEAT_SEEDS, FERTILIZER, new ItemStack(WHEAT, 2), new ItemStack(WHEAT_SEEDS), 0F, 4F, 2, 40);
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, BEETROOT_SEEDS, FERTILIZER, new ItemStack(BEETROOT, 2), new ItemStack(BEETROOT_SEEDS), 0F, 4F, 2, 40);
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, PUMPKIN_SEEDS, FERTILIZER, new ItemStack(PUMPKIN, 2), 2, 40);
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, MELON_SEEDS, FERTILIZER, new ItemStack(MELON, 2), 2, 40);
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, CARROT, FERTILIZER, new ItemStack(CARROT, 6), 2, 40);
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, POTATO, FERTILIZER, new ItemStack(POTATO, 6), 2, 40);
		for (Item item : new Item[] { COCOA_BEANS, CACTUS, SUGAR_CANE, RED_MUSHROOM, BROWN_MUSHROOM, TWISTING_VINES, WEEPING_VINES, VINE, NETHER_WART, LILY_PAD, DANDELION, POPPY, BLUE_ORCHID, ALLIUM, AZURE_BLUET, RED_TULIP, ORANGE_TULIP, WHITE_TULIP, PINK_TULIP, OXEYE_DAISY, CORNFLOWER, LILY_OF_THE_VALLEY, WITHER_ROSE, SUNFLOWER, LILAC, PEONY, ROSE_BUSH })
			addRecipe(Degree.ORDINARY, Degree.ORDINARY, item, FERTILIZER, new ItemStack(item, 4), 2, 40);
		for (Item item : new Item[] { BAMBOO, SWEET_BERRIES, KELP })
			addRecipe(Degree.ORDINARY, Degree.ORDINARY, item, FERTILIZER, new ItemStack(item, 8), 2, 40);
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, CHORUS_FLOWER, FERTILIZER, new ItemStack(CHORUS_FRUIT, 2), new ItemStack(CHORUS_FLOWER), 0F, 4F, 2, 40);
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, OAK_SAPLING, FERTILIZER, new ItemStack(OAK_LOG, 4), new ItemStack(OAK_SAPLING), 0F, 4F, 2, 40);
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, SPRUCE_SAPLING, FERTILIZER, new ItemStack(SPRUCE_LOG, 4), new ItemStack(SPRUCE_SAPLING), 0F, 4F, 2, 40);
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, BIRCH_SAPLING, FERTILIZER, new ItemStack(BIRCH_LOG, 4), new ItemStack(BIRCH_SAPLING), 0F, 4F, 2, 40);
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, JUNGLE_SAPLING, FERTILIZER, new ItemStack(JUNGLE_LOG, 4), new ItemStack(JUNGLE_SAPLING), 0F, 4F, 2, 40);
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, ACACIA_SAPLING, FERTILIZER, new ItemStack(ACACIA_LOG, 4), new ItemStack(ACACIA_SAPLING), 0F, 4F, 2, 40);
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, DARK_OAK_SAPLING, FERTILIZER, new ItemStack(DARK_OAK_LOG, 4), new ItemStack(DARK_OAK_SAPLING), 0F, 4F, 2, 40);
		{
			int c = getHashCode(Degree.ORDINARY, Degree.ORDINARY, CRIMSON_FUNGUS, FERTILIZER);
			RECIPES.put(c, Recipe.PLACE_TAKER);
			RANDOM_RECIPES.put(c, Arrays.asList(new Recipe(Degree.ORDINARY, Degree.ORDINARY, CRIMSON_FUNGUS, FERTILIZER, new ItemStack(CRIMSON_STEM, 3), new ItemStack(CRIMSON_FUNGUS), 0F, 4F, 2, 40), new Recipe(Degree.ORDINARY, Degree.ORDINARY, CRIMSON_FUNGUS, FERTILIZER, new ItemStack(NETHER_WART_BLOCK, 3), new ItemStack(CRIMSON_FUNGUS), 0F, 4F, 2, 40)));
		}
		{
			int c = getHashCode(Degree.ORDINARY, Degree.ORDINARY, WARPED_FUNGUS, FERTILIZER);
			RECIPES.put(c, Recipe.PLACE_TAKER);
			RANDOM_RECIPES.put(c, Arrays.asList(new Recipe(Degree.ORDINARY, Degree.ORDINARY, WARPED_FUNGUS, FERTILIZER, new ItemStack(WARPED_STEM, 3), new ItemStack(WARPED_FUNGUS), 0F, 4F, 2, 40), new Recipe(Degree.ORDINARY, Degree.ORDINARY, WARPED_FUNGUS, FERTILIZER, new ItemStack(WARPED_WART_BLOCK, 3), new ItemStack(WARPED_FUNGUS), 0F, 4F, 2, 40)));
		}
		Item[] fruits = new Item[] { APRICOT, BANANA, BLUEBERRY, CHERRY, CHINESE_DATE, COCONUT, GOLDEN_GRAPE, GRAPE, GRAPEFRUIT, HAWTHORN, LEMON, LONGAN, LOQUAT, LYCHEE, MANGO, ORANGE, PAYAPA, PEACH, PEAR, PERSIMMON, PLUM, POMEGRANATE, STRAWBERRY, TOMATO };
		{
			int c = getHashCode(Degree.ORDINARY, Degree.ORDINARY, FRUIT_SAPLING, FERTILIZER);
			RECIPES.put(c, Recipe.PLACE_TAKER);
			List<Recipe> list = new ArrayList<>();
			for (Item item : fruits)
				list.add(new Recipe(Degree.ORDINARY, Degree.ORDINARY, FRUIT_SAPLING, FERTILIZER, new ItemStack(item, 2), new ItemStack(FRUIT_SAPLING), 0F, 4F, 2, 40));
			RANDOM_RECIPES.put(c, list);
		}
		{
			int c = getHashCode(Degree.ORDINARY, Degree.ORDINARY, ORE_SAPLING, FERTILIZER);
			RECIPES.put(c, Recipe.PLACE_TAKER);
			List<Recipe> list = new ArrayList<>();
			for (Item item : oreRateMap.keySet())
				list.add(new Recipe(Degree.ORDINARY, Degree.ORDINARY, ORE_SAPLING, FERTILIZER, new ItemStack(item, 2), new ItemStack(ORE_SAPLING), 0F, 4F, 2, 40));
			RANDOM_RECIPES.put(c, list);
		}
		{
			int c = getHashCode(Degree.ORDINARY, Degree.ORDINARY, WOOL_SAPLING, FERTILIZER);
			RECIPES.put(c, Recipe.PLACE_TAKER);
			List<Recipe> list = new ArrayList<>();
			for (Item item : new Item[] { WHITE_WOOL, ORANGE_WOOL, MAGENTA_WOOL, LIGHT_BLUE_WOOL, YELLOW_WOOL, LIME_WOOL, PINK_WOOL, GRAY_WOOL, LIGHT_GRAY_WOOL, CYAN_WOOL, PURPLE_WOOL, BLUE_WOOL, BROWN_WOOL, GREEN_WOOL, RED_WOOL, BLACK_WOOL })
				list.add(new Recipe(Degree.ORDINARY, Degree.ORDINARY, WOOL_SAPLING, FERTILIZER, new ItemStack(item, 2), new ItemStack(WOOL_SAPLING), 0F, 4F, 2, 40));
			RANDOM_RECIPES.put(c, list);
		}
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, SAKURA_SAPLING, FERTILIZER, new ItemStack(CHERRY), new ItemStack(SAKURA_SAPLING), 2F, 5F, 0F, 4F, 2, 40);
		{
			Item[] seeds = new Item[] { LYCORIS_RADIATA_SEED, TRIFOLIUM_SEED, BLACKTHORN_SEED, CATTAIL_SEED, MARIGOLD_SEED, HIBISCUS_SEED, HYACINTH_SEED, CALAMUS_SEED, WILD_LILIUM_SEED, BAUHINIA_SEED, FLUFFY_GRASS_SEED, GERBERA_SEED, ESPARTO_SEED, GLOW_FORSYTHIA_SEED, GLAZED_SHADE_SEED, STELERA_SEED, FORAGE_CRYSTAL_SEED, ISORCHID_SEED, BURNING_CHRYSANTHE_SEED, OXALIS_SEED }, plants = new Item[] { LYCORIS_RADIATA, TRIFOLIUM, BLACKTHORN, CATTAIL, MARIGOLD, HIBISCUS, HYACINTH, CALAMUS, WILD_LILIUM, BAUHINIA, FLUFFY_GRASS, GERBERA, ESPARTO, GLOW_FORSYTHIA, GLAZED_SHADE, STELERA, FORAGE_CRYSTAL, ISORCHID, BURNING_CHRYSANTHE, OXALIS };
			for (int i = 0; i < seeds.length; i++) {
				addRecipe(Degree.ORDINARY, Degree.ORDINARY, seeds[i], FERTILIZER, new ItemStack(plants[i]), 2, 40);
				addRecipe(Degree.ORDINARY, Degree.ORDINARY, plants[i], FERTILIZER, new ItemStack(plants[i], 4), 2, 40);
			}
		}
		/**
		 * 常温低压
		 */
		addRecipe(Degree.ORDINARY, Degree.LOW, PHANTOM_MEMBRANE, POTION, new ItemStack(AGENTIA_OF_LIGHTNESS), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, GOLDEN_CARROT, POTION, new ItemStack(AGENTIA_OF_EYESIGHT), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, MAGMA_CREAM, POTION, new ItemStack(AGENTIA_OF_FIRE_SHIELD), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, PUFFERFISH, POTION, new ItemStack(AGENTIA_OF_WATERLESS), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, ENDER_PEARL, POTION, new ItemStack(AGENTIA_OF_TRANSPARENTNESS), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, RABBIT_FOOT, POTION, new ItemStack(AGENTIA_OF_BOUNCE), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, SUGAR, POTION, new ItemStack(AGENTIA_OF_SHARPNESS), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, BLAZE_POWDER, POTION, new ItemStack(AGENTIA_OF_BLOOD), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, GHAST_TEAR, POTION, new ItemStack(AGENTIA_OF_EXTREMENESS), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, GLISTERING_MELON_SLICE, POTION, new ItemStack(AGENTIA_OF_SHIELD), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, NAUTILUS_SHELL, POTION, new ItemStack(AGENTIA_OF_TIDE), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, ENCHANTED_IRON, POTION, new ItemStack(AGENTIA_OF_CHAIN), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, ENCHANTED_DIAMOND, POTION, new ItemStack(AGENTIA_OF_SHIELD), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, WING_OF_SKY, POTION, new ItemStack(AGENTIA_OF_SKY), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, HEART_OF_CRYSTAL, POTION, new ItemStack(AGENTIA_OF_OCEAN), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, LIMB_OF_RIDGE, POTION, new ItemStack(AGENTIA_OF_RIDGE), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, SOUL_OF_DIRT, POTION, new ItemStack(AGENTIA_OF_DIRT), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, CERTIFICATION_OF_EARTH, POTION, new ItemStack(AGENTIA_OF_EARTH), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, NOVA_OF_FIRE, POTION, new ItemStack(AGENTIA_OF_FIRE), 4, 200);
		addRecipe(Degree.ORDINARY, Degree.LOW, SPIRIT_OF_LIFE, POTION, new ItemStack(AGENTIA_OF_LIFE), 4, 200);
		/*
		 * 低温高压
		 */
		for (Item item : fruits)
			addRecipe(Degree.LOW, Degree.HIGH, HONEY_BOTTLE, item, new ItemStack(COLD_DRINK), 2, 100);
		addRecipe(Degree.LOW, Degree.HIGH, HONEY_BOTTLE, APPLE, new ItemStack(COLD_DRINK), 2, 100);
		addRecipe(Degree.LOW, Degree.HIGH, POTION, AMMONIA_REFRIGERANT, new ItemStack(ICE), new ItemStack(AMMONIA_REFRIGERANT), 1, 20);
		addRecipe(Degree.LOW, Degree.HIGH, GREEN_FORCE_OF_WATER, AMMONIA_REFRIGERANT, new ItemStack(ICE), new ItemStack(AMMONIA_REFRIGERANT), 1, 20);
		/**
		 * 低温常压
		 */
		addRecipe(Degree.LOW, Degree.ORDINARY, LAVA_BUCKET, COBBLESTONE_PLUGIN, new ItemStack(COBBLESTONE, 4), 1, 20);
		addRecipe(Degree.LOW, Degree.ORDINARY, LAVA_BUCKET, STONE_PLUGIN, new ItemStack(STONE, 4), 1, 20);
		addRecipe(Degree.LOW, Degree.ORDINARY, LAVA_BUCKET, BLACKSTONE_PLUGIN, new ItemStack(BLACKSTONE, 4), 1, 20);
		addRecipe(Degree.LOW, Degree.ORDINARY, LAVA_BUCKET, NETHERRACK_PLUGIN, new ItemStack(NETHERRACK, 4), 1, 20);
		addRecipe(Degree.LOW, Degree.ORDINARY, LAVA_BUCKET, END_STONE_PLUGIN, new ItemStack(END_STONE, 1), 1, 20);
		addRecipe(Degree.LOW, Degree.ORDINARY, LAVA_BUCKET, BASALT_PLUGIN, new ItemStack(BASALT, 3), 1, 20);
		/*
		 * 低温低压
		 */
		addRecipe(Degree.LOW, Degree.LOW, POTION, AMMONIA_REFRIGERANT, new ItemStack(SNOW_BLOCK), new ItemStack(AMMONIA_REFRIGERANT), 1, 20);
		addRecipe(Degree.LOW, Degree.LOW, GREEN_FORCE_OF_WATER, AMMONIA_REFRIGERANT, new ItemStack(SNOW_BLOCK), new ItemStack(AMMONIA_REFRIGERANT), 1, 20);
		addRecipe(Degree.LOW, Degree.LOW, WATER_BUCKET, AMMONIA_REFRIGERANT, new ItemStack(POWDER_SNOW_BUCKET), new ItemStack(AMMONIA_REFRIGERANT), 1, 20);

	}

	public enum Degree {
		ORDINARY, LOW, HIGH
	}

	public static class Recipe {
		public static final Recipe PLACE_TAKER = new Recipe(null, null, AIR, AIR, ItemStack.EMPTY, ItemStack.EMPTY, 0, 0, 0, 0);
		public final Degree temperature;
		public final Degree pressure;
		public final Item input1, input2;
		public final ItemStack output1, output2;
		public final float count1Min, count1Max, count2Min, count2Max;
		public final int experience, time;

		public Recipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, ItemStack output2, float count2, int experience, int time) {
			this(temperature, pressure, input1, input2, output1, output2, count2, count2 + 1, experience, time);
		}

		public Recipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, int experience, int time) {
			this(temperature, pressure, input1, input2, output1, ItemStack.EMPTY, 0, 0, experience, time);
		}

		public Recipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, ItemStack output2, float count2Min, float count2Max, int experience, int time) {
			this(temperature, pressure, input1, input2, output1, output2, output1.getCount(), output1.getCount(), count2Min, count2Max, experience, time);
		}

		public Recipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, ItemStack output2, float count1Min, float count1Max, float count2Min, float count2Max, int experience, int time) {
			this.temperature = temperature;
			this.pressure = pressure;
			this.input1 = input1;
			this.input2 = input2;
			this.output1 = output1;
			this.output2 = output2;
			this.count1Min = count1Min;
			this.count1Max = count1Max;
			this.count2Min = count2Min;
			this.count2Max = count2Max;
			this.experience = experience;
			this.time = time;
		}

		@Override
		public String toString() {
			return String.format("%s,%s,%s,%s,%d-%s,%s,%d", temperature, pressure, input1, input2, experience, output1, output2, time);
		}

		public ItemStack[] output() {
			ItemStack[] rst = new ItemStack[2];
			rst[0] = output1.copy();
			rst[1] = output2.copy();
			if (count1Min == count1Max) {
				rst[0].setCount((int) count1Max);
			} else {
				rst[0].setCount(certianCount(count1Min, count1Max));
			}
			if (!output2.isEmpty()) {
				if (count2Min == count2Max) {
					rst[1].setCount((int) count2Max);
				} else {
					rst[1].setCount(certianCount(count2Min, count2Max));
				}
			}
			return rst;
		}

		public static int certianCount(float min, float max) {
			int rst = (int) Math.floor(min + Math.random() * (max - min));
			return rst;
		}
	}

}
