package cpp.block.entity;

import static cpp.init.CppBlocks.FRUIT_SAPLING;
import static cpp.init.CppBlocks.ORE_SAPLING;
import static cpp.init.CppBlocks.RARE_EARTH_GLASS;
import static cpp.init.CppBlocks.REINFORCED_GLASS;
import static cpp.init.CppBlocks.SAKURA_SAPLING;
import static cpp.init.CppBlocks.WOOL_SAPLING;
import static cpp.init.CppItems.ACID;
import static cpp.init.CppItems.AGENTIA_OF_BLOOD;
import static cpp.init.CppItems.AGENTIA_OF_BOUNCE;
import static cpp.init.CppItems.AGENTIA_OF_CHAIN;
import static cpp.init.CppItems.AGENTIA_OF_DIRT;
import static cpp.init.CppItems.AGENTIA_OF_EARTH;
import static cpp.init.CppItems.AGENTIA_OF_EXTREMENESS;
import static cpp.init.CppItems.AGENTIA_OF_EYESIGHT;
import static cpp.init.CppItems.AGENTIA_OF_FIRE;
import static cpp.init.CppItems.AGENTIA_OF_FIRE_SHIELD;
import static cpp.init.CppItems.AGENTIA_OF_LIFE;
import static cpp.init.CppItems.AGENTIA_OF_LIGHTNESS;
import static cpp.init.CppItems.AGENTIA_OF_OCEAN;
import static cpp.init.CppItems.AGENTIA_OF_RIDGE;
import static cpp.init.CppItems.AGENTIA_OF_SHARPNESS;
import static cpp.init.CppItems.AGENTIA_OF_SHIELD;
import static cpp.init.CppItems.AGENTIA_OF_SKY;
import static cpp.init.CppItems.AGENTIA_OF_TIDE;
import static cpp.init.CppItems.AGENTIA_OF_TRANSPARENTNESS;
import static cpp.init.CppItems.AGENTIA_OF_WATERLESS;
import static cpp.init.CppItems.ALKALOID;
import static cpp.init.CppItems.ALKALOID_RARE_EARTH;
import static cpp.init.CppItems.AMMONIA_REFRIGERANT;
import static cpp.init.CppItems.APRICOT;
import static cpp.init.CppItems.BANANA;
import static cpp.init.CppItems.BASALT_PLUGIN;
import static cpp.init.CppItems.BIONIC_ACID;
import static cpp.init.CppItems.BLACKSTONE_PLUGIN;
import static cpp.init.CppItems.BLUEBERRY;
import static cpp.init.CppItems.BOTTLE_OF_AIR;
import static cpp.init.CppItems.BOTTLE_OF_SALT;
import static cpp.init.CppItems.CARBON_DUST;
import static cpp.init.CppItems.CERTIFICATION_OF_EARTH;
import static cpp.init.CppItems.CHERRY;
import static cpp.init.CppItems.CHINESE_DATE;
import static cpp.init.CppItems.CINDER;
import static cpp.init.CppItems.COARSE_SILICON;
import static cpp.init.CppItems.COBBLESTONE_PLUGIN;
import static cpp.init.CppItems.COCONUT;
import static cpp.init.CppItems.COLD_DRINK;
import static cpp.init.CppItems.COPPER_DUST;
import static cpp.init.CppItems.DIAMOND_DUST;
import static cpp.init.CppItems.EMERALD_DUST;
import static cpp.init.CppItems.ENCHANTED_DIAMOND;
import static cpp.init.CppItems.ENCHANTED_IRON;
import static cpp.init.CppItems.END_STONE_PLUGIN;
import static cpp.init.CppItems.FERTILIZER;
import static cpp.init.CppItems.GOLDEN_GRAPE;
import static cpp.init.CppItems.GOLD_DUST;
import static cpp.init.CppItems.GRAPE;
import static cpp.init.CppItems.GRAPEFRUIT;
import static cpp.init.CppItems.GREEN_FORCE_OF_WATER;
import static cpp.init.CppItems.HAWTHORN;
import static cpp.init.CppItems.HEART_OF_CRYSTAL;
import static cpp.init.CppItems.IRON_DUST;
import static cpp.init.CppItems.LEMON;
import static cpp.init.CppItems.LIMB_OF_RIDGE;
import static cpp.init.CppItems.LONGAN;
import static cpp.init.CppItems.LOQUAT;
import static cpp.init.CppItems.LYCHEE;
import static cpp.init.CppItems.MANGO;
import static cpp.init.CppItems.NETHERRACK_PLUGIN;
import static cpp.init.CppItems.NOVA_OF_FIRE;
import static cpp.init.CppItems.ORANGE;
import static cpp.init.CppItems.PAYAPA;
import static cpp.init.CppItems.PEACH;
import static cpp.init.CppItems.PEAR;
import static cpp.init.CppItems.PERSIMMON;
import static cpp.init.CppItems.PLUM;
import static cpp.init.CppItems.POMEGRANATE;
import static cpp.init.CppItems.QUARTZ_DUST;
import static cpp.init.CppItems.RARE_EARTH_DUST;
import static cpp.init.CppItems.RARE_EARTH_SALT;
import static cpp.init.CppItems.SILICON_DUST;
import static cpp.init.CppItems.SILICON_PLATE;
import static cpp.init.CppItems.SODA_WATER;
import static cpp.init.CppItems.SOUL_OF_DIRT;
import static cpp.init.CppItems.SPIRIT_OF_LIFE;
import static cpp.init.CppItems.STEEL_DUST;
import static cpp.init.CppItems.STONE_PLUGIN;
import static cpp.init.CppItems.STRAWBERRY;
import static cpp.init.CppItems.TOMATO;
import static cpp.init.CppItems.WING_OF_SKY;
import static net.minecraft.item.Items.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.nbt.NbtCompound;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Maps;

import cpp.api.Utils;
import cpp.block.AllInOneMachineBlock;
import cpp.block.FlowerGrass1Block;
import cpp.init.CppBlockEntities;
import cpp.init.CppBlockTags;
import cpp.init.CppBlocks;
import cpp.init.CppItemTags;
import cpp.init.CppRecipes;
import cpp.item.RedForceOfFire;
import cpp.recipe.AllInOneMachineRecipe;
import cpp.screen.handler.AllInOneMachineScreenHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.ItemTags;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

/**
 * 多功能一体机
 * 
 * @author Ph-苯
 *
 */
public class AllInOneMachineBlockEntity extends AExpMachineBlockEntity {
	@Deprecated
	public static final Map<Triple<Degree, Degree, Set<Item>>, AllInOneMachineRecipe> RECIPES2 = Maps.newHashMap();
	private static final Map<Item, Double> ORE_BASIC_COUNTS = Maps.newLinkedHashMap();
	private static final Map<Set<Item>, Double> ORE_RATES = Maps.newLinkedHashMap();
	private static final Map<Set<Item>, AllInOneMachineRecipe> ORE_RECIPES = Maps.newLinkedHashMap();
	private static final int[] AVAILABLE_SLOTS = new int[] { 0, 1, 2 };
	@Deprecated
	private static final Map<Integer, Recipe> RECIPES = new HashMap<>();
	@Deprecated
	private static final Map<Integer, List<Recipe>> RANDOM_RECIPES = new HashMap<>();
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
			return super.size() + 3;
		}

		@Override
		public void set(int index, int value) {
			switch (index - super.size()) {
			case 2:
				setTemperature(Degree.values()[value / 16 % Degree.values().length]);
				setPressure(Degree.values()[value % 16 % Degree.values().length]);
				break;
			case 1:
				availabeTemperature.clear();
				for (int i = 0; value > 0 && i < Degree.values().length; i++) {
					if ((value & 1) == 1)
						availabeTemperature.add(Degree.values()[i]);
					value >>= 1;
				}
//				availabeTemperatureI = value;
				break;
			case 0:
				availabePressure.clear();
				for (int i = 0; value > 0 && i < Degree.values().length; i++) {
					if ((value & 1) == 1)
						availabePressure.add(Degree.values()[i]);
					value >>= 1;
				}
//				availabePressureI = value;
				break;
			default:
				super.set(index, value);
			}
		}

		@Override
		public int get(int index) {
			switch (index - super.size()) {
			case 2:
				return temperature.ordinal() * 16 + pressure.ordinal();
			case 1: {
				int a = 0;
				for (Degree degree : availabeTemperature) {
					a |= 1 << degree.ordinal();
				}

				return a;
			}
			case 0: {
				int a = 0;
				for (Degree degree : availabePressure) {
					a |= 1 << degree.ordinal();
				}
				return a;
			}
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
//		availabePressure.add(Degree.LOW);
//		availabePressure.add(Degree.HIGH);
//		availabeTemperature.add(Degree.LOW);
//		availabeTemperature.add(Degree.HIGH);
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
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, inventory);
		for (int i = 0, a = nbt.getInt("availabeTemperature"); a > 0 && i < Degree.values().length; i++) {
			if ((a & 1) == 1)
				availabeTemperature.add(Degree.values()[i]);
			a >>= 1;
		}
		for (int i = 0, a = nbt.getInt("availabePressure"); a > 0 && i < Degree.values().length; i++) {
			if ((a & 1) == 1)
				availabePressure.add(Degree.values()[i]);
			a >>= 1;
		}
		propertyDelegate.set(6, nbt.getInt("temperaturePressure"));
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, inventory);
		nbt.putInt("temperaturePressure", propertyDelegate.get(6));
		int a = 0;
		for (Degree degree : availabeTemperature) {
			a |= 1 << degree.ordinal();
		}
		nbt.putInt("availabeTemperature", a);
		a = 0;
		for (Degree degree : availabePressure) {
			a |= 1 << degree.ordinal();
		}
		nbt.putInt("availabePressure", a);
		return nbt;
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new AllInOneMachineScreenHandler(syncId, playerInventory, this);
	}

	public static void tick(World world, BlockPos pos, BlockState state, AllInOneMachineBlockEntity blockEntity) {
		if (!world.isClient) {
			blockEntity.transferExpBottle();
			AllInOneMachineRecipe recipe;
			if (blockEntity.getTemperature() == Degree.HIGH && blockEntity.getPressure() == Degree.HIGH && (recipe = ORE_RECIPES.get(blockEntity.ingredient())) != null) {
				if (!recipe.matches(blockEntity, world))
					recipe = null;
			} else {
				recipe = blockEntity.getRecipe();
			}
//			System.out.println(recipe);
			if (recipe != null) {
//				System.out.println(recipe.ingredient);
				blockEntity.workTimeTotal = recipe.time;
				if (Utils.canInsert(recipe.maximize(blockEntity), blockEntity, 3, 5)) {
					if (blockEntity.workTime >= blockEntity.workTimeTotal) {
						blockEntity.workTime = 0;
						blockEntity.expStorage -= recipe.experience;
						for (int i = 1; i < 3; i++) {
							if (!AllInOneMachineRecipe.UNCONSUMABLE.contains(blockEntity.getStack(i).getItem())) {
								blockEntity.getStack(i).decrement(1);
							}
						}
//						System.out.println(recipe.produce(blockEntity));
						Utils.insert(recipe.produce(blockEntity), blockEntity, 3, 5);
					} else {
						blockEntity.workTime++;
					}
				}
			} else {
				blockEntity.workTimeTotal = 0;
				blockEntity.workTime = 0;
			}

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
		super.onOpen(player);
//		if (!player.world.isClient&&player.world.isClient) {
//			try {
//				for (Recipe recipe : RECIPES.values()) {
//					if (recipe == null || recipe.temperature == Degree.HIGH && recipe.pressure == Degree.HIGH) {
//						continue;
//					}
//					File file = new File(String.format("D:\\CCC\\Documents\\编程\\Minecraft Mod\\更多的合成 FabricMod 开发中\\src\\main\\resources\\data\\cpp\\recipes\\all_in_one_machine\\%s_%s\\%s%s.json", recipe.temperature.toString().toLowerCase(), recipe.pressure.toString().toLowerCase(), Registry.ITEM.getId(recipe.output1.getItem()).getPath(), recipe.output2.isEmpty() ? "" : "_and_" + Registry.ITEM.getId(recipe.output2.getItem()).getPath()));
//					if (!file.getParentFile().exists()) {
//						file.getParentFile().mkdir();
//					}
//					FileWriter fw = new FileWriter(file);
//					fw.write(String.format("{\r\n	\"type\": \"cpp:all_in_one_machine\",\r\n	\"temperature\": \"%s\",\r\n	\"pressure\": \"%s\",\r\n	\"ingredients\": [\r\n		\"%s\"", recipe.temperature.toString().toLowerCase(), recipe.pressure.toString().toLowerCase(), Registry.ITEM.getId(recipe.input1)));
//					if (recipe.input2 != AIR) {
//						fw.write(String.format(",%n		\"%s\"\r\n	],%n", Registry.ITEM.getId(recipe.input2)));
//					}
//					fw.write(String.format("	\"experience\": %d,%n", recipe.experience));
//					fw.write(String.format("	\"time\": %d,%n", recipe.time));
//					fw.write("	\"products\": [\r\n		");
//					double c1 ;
//					if (recipe.count1Max == recipe.count1Min) {
//						c1 = recipe.count1Max;
//					}else {
//						c1 = (recipe.count1Max + recipe.count1Min-1) / 2;
//					}
//					if (c1 == 1) {
//						fw.write(String.format("\"%s\"", Registry.ITEM.getId(recipe.output1.getItem())));
//					} else {
//						fw.write(String.format("{\r\n			\"item\": \"%s\",\r\n			\"count\": %s\r\n		}", Registry.ITEM.getId(recipe.output1.getItem()), new BigDecimal(c1).stripTrailingZeros().toPlainString()));
//					}
//					if (!recipe.output2.isEmpty()) {
//						double c2 ;
//						if (recipe.count2Max == recipe.count2Min) {
//							c2 = recipe.count2Max;
//						}else {
//							c2 = (recipe.count2Max + recipe.count2Min-1) / 2;
//						}
//						if (c2 == 1) {
//							fw.write(String.format(",%n		\"%s\"", Registry.ITEM.getId(recipe.output2.getItem())));
//						} else {
//							fw.write(String.format(",%n		{\r\n			\"item\": \"%s\",\r\n			\"count\": %s\r\n		}", Registry.ITEM.getId(recipe.output2.getItem()), new BigDecimal(c2).setScale(2, RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()));
//						}
//					}
//					fw.write("\r\n	]\r\n}");
//					fw.close();
//
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
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
//		System.out.println(String.format("slot:%d stack:%s", slot, stack));
		if (stack.isOf(EXPERIENCE_BOTTLE)) {
//			System.out.println(1);
			return slot == 0;
		}
		return (slot == 1 && stack.getItem() != getStack(2).getItem()) || (slot == 2 && stack.getItem() != getStack(1).getItem());
	}

	/*
	 * 以下是自定义方法
	 */
	/**
	 * 获取当前温度
	 * 
	 * @return
	 */
	public Degree getTemperature() {
		return temperature;
	}

	/**
	 * 设置温度
	 * 
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
	 * 
	 * @param degree
	 * @return
	 */
	public boolean addAvailableTemperature(Degree degree) {
//		System.out.println(availabeTemperature);
		return availabeTemperature.add(degree);
//		if (Degree.has(availabeTemperatureI, degree)) {
//			return false;
//		}else {
//			availabeTemperatureI|=Degree.code(degree);
//			return true;
//		}
	}

	public Set<Degree> getAvailabeTemperature() {
		return EnumSet.copyOf(availabeTemperature);
	}

	public boolean isAvailabeTemperature(Degree degree) {
		return availabeTemperature.contains(degree);
	}

	/**
	 * 获取当前压强
	 * 
	 * @return
	 */
	public Degree getPressure() {
		return pressure;
	}

	/**
	 * 设置压强
	 * 
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
	 * 
	 * @param degree 压强
	 * @return
	 */
	public boolean addAvailablePressure(Degree degree) {
		return availabePressure.add(degree);
//		if (Degree.has(availabePressureI, degree)) {
//			return false;
//		}else {
//			availabePressureI|=Degree.code(degree);
//			return true;
//		}
	}

	public Set<Degree> getAvailabePressure() {
		return EnumSet.copyOf(availabePressure);
	}

	public boolean isAvailabePressure(Degree degree) {
		return availabePressure.contains(degree);
	}

	public AllInOneMachineRecipe getRecipe() {
		if (!getWorld().isClient) {
			return getWorld().getServer().getRecipeManager().getFirstMatch(CppRecipes.ALL_IN_ONE_MACHINE_TYPE, this, world).orElse(null);
		}
		return null;
	}

	@Deprecated
	/**
	 * 根据原料和温度压强计算哈希码，便于查找配方
	 * 
	 * @param temperature 温度
	 * @param pressure    压强
	 * @param input1      原料1
	 * @param input2      原料2
	 * @return 哈希码
	 */

	public static int getHashCode(Degree temperature, Degree pressure, Item input1, Item input2) {
		return (input1.hashCode() ^ input2.hashCode()) * 256 + temperature.ordinal() * 16 + pressure.ordinal();
	}

	@Deprecated
	private static void addRecipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, ItemStack output2, int experience, int time) {
		addRecipe(temperature, pressure, input1, input2, output1, output2, output2.getCount(), output2.getCount(), experience, time);
	}

	@Deprecated
	private static void addRecipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, int experience, int time) {
		addRecipe(temperature, pressure, input1, input2, output1, ItemStack.EMPTY, 0, experience, time);
	}

	@Deprecated
	private static void addRecipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, ItemStack output2, float count2, int experience, int time) {
		addRecipe(temperature, pressure, input1, input2, output1, output2, count2, count2 + 1, experience, time);
	}

	@Deprecated
	private static void addRecipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, ItemStack output2, float count2Min, float count2Max, int experience, int time) {
		addRecipe(temperature, pressure, input1, input2, output1, output2, output1.getCount(), output1.getCount(), count2Min, count2Max, experience, time);
	}

	@Deprecated
	private static void addRecipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, ItemStack output2, float count1Min, float count1Max, float count2Min, float count2Max, int experience, int time) {
		RECIPES.put(getHashCode(temperature, pressure, input1, input2), new Recipe(temperature, pressure, input1, input2, output1, output2, count1Min, count1Max, count2Min, count2Max, experience, time));
	}

	static {
		{
			ORE_BASIC_COUNTS.put(LAPIS_ORE, 6.);
			ORE_BASIC_COUNTS.put(REDSTONE_ORE, 5.);
		}
		ServerLifecycleEvents.SERVER_STARTED.register(AllInOneMachineBlockEntity::load);
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((MinecraftServer server, ServerResourceManager serverResourceManager, boolean success) -> {
			if (success) {
				load(server);
			}
		});
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
		for (Item sand : new Item[] { SAND, RED_SAND }) {
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
			int c = getHashCode(Degree.ORDINARY, Degree.ORDINARY, FRUIT_SAPLING.asItem(), FERTILIZER);
			RECIPES.put(c, Recipe.PLACE_TAKER);
			List<Recipe> list = new ArrayList<>();
			for (Item item : fruits)
				list.add(new Recipe(Degree.ORDINARY, Degree.ORDINARY, FRUIT_SAPLING.asItem(), FERTILIZER, new ItemStack(item, 2), new ItemStack(FRUIT_SAPLING), 0F, 4F, 2, 40));
			RANDOM_RECIPES.put(c, list);
		}
		{
			int c = getHashCode(Degree.ORDINARY, Degree.ORDINARY, ORE_SAPLING.asItem(), FERTILIZER);
			RECIPES.put(c, Recipe.PLACE_TAKER);
			List<Recipe> list = new ArrayList<>();
			for (Item item : oreRateMap.keySet())
				list.add(new Recipe(Degree.ORDINARY, Degree.ORDINARY, ORE_SAPLING.asItem(), FERTILIZER, new ItemStack(item, 2), new ItemStack(ORE_SAPLING), 0F, 4F, 2, 40));
			RANDOM_RECIPES.put(c, list);
		}
		{
			int c = getHashCode(Degree.ORDINARY, Degree.ORDINARY, WOOL_SAPLING.asItem(), FERTILIZER);
			RECIPES.put(c, Recipe.PLACE_TAKER);
			List<Recipe> list = new ArrayList<>();
			for (Item item : ItemTags.WOOL.values())
				list.add(new Recipe(Degree.ORDINARY, Degree.ORDINARY, WOOL_SAPLING.asItem(), FERTILIZER, new ItemStack(item, 2), new ItemStack(WOOL_SAPLING), 0F, 4F, 2, 40));
			RANDOM_RECIPES.put(c, list);
		}
		addRecipe(Degree.ORDINARY, Degree.ORDINARY, SAKURA_SAPLING.asItem(), FERTILIZER, new ItemStack(CHERRY), new ItemStack(SAKURA_SAPLING), 2F, 5F, 0F, 4F, 2, 40);
		{
			List<Block> plants = CppBlockTags.FLOWER_GRASSES_1.values();
			for (Block plant : plants) {
				addRecipe(Degree.ORDINARY, Degree.ORDINARY, ((FlowerGrass1Block) plant).getSeed(), FERTILIZER, new ItemStack(plant), 2, 40);
				addRecipe(Degree.ORDINARY, Degree.ORDINARY, plant.asItem(), FERTILIZER, new ItemStack(plant, 4), 2, 40);
			}
		}
		// 常温低压
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

		{

		}
	}

	public enum Degree {
		ORDINARY, LOW, HIGH;
		public static int code(Degree degree) {
			int a = 0;
			for (int i = values().length - 1; i >= 0; i--) {
				if (degree == values()[i])
					a |= 1;
				a <<= 1;
			}
			return a;
		}

		public static boolean has(int code, Degree degree) {
			for (int i = 0; i < values().length; i++) {
				if ((code & 1) == 1 && degree == values()[i]) {
					return true;
				}
				code >>= 1;
			}
			return false;
		}
	}

	@Deprecated
	public static class Recipe {
		public static final Recipe PLACE_TAKER = new Recipe(Degree.ORDINARY, Degree.ORDINARY, AIR, AIR, ItemStack.EMPTY, ItemStack.EMPTY, 0, 0, 0, 0);
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
			this(temperature, pressure, input1, input2, output1, output2, output1.getCount(), output2.getCount(), count2Min, count2Max, experience, time);
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
			return (int) Math.floor(min + Math.random() * (max - min));
		}
	}

	public Set<Item> ingredient() {
		Builder<Item> builder = ImmutableSet.builder();
		for (int i = 1; i < 3; i++) {
			if (!getStack(i).isEmpty()) {
				builder.add(getStack(i).getItem());
			}
		}
		return builder.build();
	}

	@Deprecated
	public static Set<Item> ingredient(Item item1, Item item2) {
		if (item2 == null)
			return ImmutableSet.of(item1);
		return ImmutableSet.of(item1, item2);
	}

	private static void load(MinecraftServer server) {
		ServerWorld world = server.getWorlds().iterator().next();
		// 读取矿石倍率，生成矿石配方
		ORE_RATES.clear();
		ORE_RECIPES.clear();
		ServerCommandSource commandSource = new ServerCommandSource(CommandOutput.DUMMY, Vec3d.ZERO, Vec2f.ZERO, world, 4, "更多的合成：多功能一体机：读取矿石倍率", LiteralText.EMPTY, server, null);
		ImmutableList<Item> ores = ImmutableList.<Item>builder().addAll(ImmutableSet.<Item>builder().addAll(Utils.findByKeyword(Registry.ITEM, "_ore")).addAll(CppItemTags.ORES.values()).build()).build();
		for (int i = 0; i < ores.size(); i++) {
			Item item1 = ores.get(i);
			String id1 = Registry.ITEM.getId(item1).toString();
			for (int j = i + 1; j < ores.size(); j++) {
				Item item2 = ores.get(j);
				String id2 = Registry.ITEM.getId(item2).toString();
				double rate = server.getCommandManager().execute(commandSource, String.format("data get storage cpp:constants allInOneMachine.oreRates.%s+%s 10000", id1, id2)) / 10000.;
				if (rate == 0) {
					rate = server.getCommandManager().execute(commandSource, String.format("data get storage cpp:constants allInOneMachine.oreRates.%s+%s 10000", id2, id1)) / 10000.;
					if (rate == 0) {
						rate = world.random.nextDouble() * 3 + 1;
						server.getCommandManager().execute(commandSource, String.format("data modify storage cpp:constants allInOneMachine.oreRates.%s+%s set value %f", id1, id2, rate));
					}
				}
				Set<Item> set = ImmutableSet.of(item1, item2);
				ORE_RATES.put(set, rate);
				ORE_RECIPES.put(set, new AllInOneMachineRecipe(new Identifier(String.format("cpp:all_in_one_machine/%s_and%s", Registry.ITEM.getId(item1).getPath(), Registry.ITEM.getId(item2).getPath())), Degree.HIGH, Degree.HIGH, ImmutableList.of(Pair.of(ImmutableList.of(item1), null), Pair.of(ImmutableList.of(item2), null)), 4, 200, ImmutableList.of(Pair.of(ImmutableList.of(RedForceOfFire.smelt(item1.getDefaultStack(), server, null).getItem()), ORE_BASIC_COUNTS.getOrDefault(item1, 1.) * rate), Pair.of(ImmutableList.of(RedForceOfFire.smelt(item2.getDefaultStack(), server, null).getItem()), ORE_BASIC_COUNTS.getOrDefault(item2, 1.) * rate))));
			}
		}
	}
}
