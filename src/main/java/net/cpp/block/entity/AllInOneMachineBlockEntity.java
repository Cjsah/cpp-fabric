package net.cpp.block.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.cpp.gui.handler.AllInOneMachineScreenHandler;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

import static net.minecraft.item.Items.*;

public class AllInOneMachineBlockEntity
		extends AMachineBlockEntity /* implements RecipeUnlocker, RecipeInputProvider */ {
//	private static final int[] AVAILABLE_SLOTS = new int[] { 0, 1, 2 };
	private static final Map<Integer, Recipe> RECIPES = new HashMap<>();
	private static final Map<Item, ItemStack> ITEM_BUFFER = new HashMap<>();
	private int workTime = 0;
	private int expStorage = 0;
	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
	private Degree temperature = Degree.ORDINARY;
	private Degree pressure = Degree.ORDINARY;
	/**
	 * 可用的温度
	 */
	private Set<Degree> availabeTemperature = new HashSet<>(3);
	/**
	 * 可用的压强
	 */
	private Set<Degree> availabePressure = new HashSet<>(3);
	private final Object2IntOpenHashMap<Identifier> recipesUsed = new Object2IntOpenHashMap<Identifier>();
	public final PropertyDelegate propertyDelegate = new PropertyDelegate() {

		@Override
		public int size() {
			return 4;
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
			case 0:
				setOutputDir(IOutputDiractionalBlockEntity.byteToDir((byte) value));
				break;
			case 1:
				workTime = value;
				break;
			case 2:
				expStorage = value;
				break;
			case 3:
				setTemperature(Degree.values()[value / 16 % Degree.values().length]);
				setPressure(Degree.values()[value % 16 % Degree.values().length]);
				break;
			default:
				break;
			}
		}

		@Override
		public int get(int index) {
			switch (index) {
			case 0:
				return dirToByte();
			case 1:
				return workTime;
			case 2:
				return expStorage;
			case 3:
				return temperature.ordinal() * 16 + pressure.ordinal();
			default:
				return -1;
			}
		}
	};

	public AllInOneMachineBlockEntity() {
		super(CppBlockEntities.ALL_IN_ONE_MACHINE);
		availabeTemperature.add(Degree.ORDINARY);
		availabePressure.add(Degree.ORDINARY);
		availabePressure.add(Degree.HIGH);
	}

	/*
	 * 以下是AMachineBlock的方法
	 */
	@Override
	public Text getTitle() {
		return CppBlocks.ALL_IN_ONE_MACHINE.getName();
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
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		Inventories.fromTag(tag, inventory);
		workTime = tag.getInt("processTime");
		expStorage = tag.getInt("expStorage");
		propertyDelegate.set(3, tag.getByte("temperaturePressure"));
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
		tag.putInt("processTime", workTime);
		tag.putInt("expStorage", expStorage);
		tag.putByte("temperaturePressure", (byte) propertyDelegate.get(3));
		int a = 0;
		for (Degree degree: availabeTemperature) {
			a |= 1 << degree.ordinal();
		}
		tag.putInt("availabeTemperature", a);
		a = 0;
		for (Degree degree: availabePressure) {
			a |= 1 << degree.ordinal();
		}
		tag.putInt("availabePressure", a);
		return tag;
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new AllInOneMachineScreenHandler(syncId, playerInventory, this);
	}

	/*
	 * 以下是IOutputDiractionalBlockEntity的方法
	 */
	@Override
	public void shiftOutputDir() {
		propertyDelegate.set(0, dirToByte() + 1);
	}

	/*
	 * 以下是SidedInventory的方法
	 */
//	@Override
//	public int[] getAvailableSlots(Direction side) {
//		return AVAILABLE_SLOTS;
//	}
//
//	@Override
//	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
//		return slot == 0 && !getStack(1).getItem().equals(stack.getItem())
//				|| slot == 1 && !getStack(0).getItem().equals(stack.getItem())
//				|| slot == 2 && stack.getItem().equals(EXPERIENCE_BOTTLE);
//	}
//
//	@Override
//	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
//		return false;
//	}

	/*
	 * 以下是RecipeUnlocker的方法
	 */
//	@Override
//	public void setLastRecipe(Recipe<?> recipe) {
//		if (recipe != null) {
//			Identifier identifier = recipe.getId();
//			recipesUsed.addTo(identifier, 1);
//		}
//	}
//
//	@Override
//	public Recipe<?> getLastRecipe() {
//		return null;
//	}
//
//	/*
//	 * 以下是RecipeInputProvider的方法
//	 */
//	@Override
//	public void provideRecipeInputs(RecipeFinder finder) {
//		Iterator<ItemStack> var2 = this.inventory.iterator();
//		while (var2.hasNext()) {
//			ItemStack itemStack = (ItemStack) var2.next();
//			finder.addItem(itemStack);
//		}
//	}

	/*
	 * 以下是Tickable的方法
	 */
	@Override
	public void tick() {
		if (!world.isClient) {
			if (!getStack(2).isEmpty() && expStorage <= 91) {
				getStack(2).increment(-1);
				expStorage += 9;
			}
			if (getStack(0).isEmpty() && getStack(1).isEmpty()) {
				workTime = 0;
			} else {
				Recipe recipe = getRecipe();
				if (recipe.temperature == temperature && recipe.pressure == pressure
						&& recipe.input1 == getStack(0).getItem() && recipe.input2 == getStack(1).getItem()) {
					ItemStack[] outputs = recipe.output();
					if (getStack(0).isItemEqualIgnoreDamage(outputs[0])
							&& getStack(1).isItemEqualIgnoreDamage(outputs[1])
							&& getStack(0).getCount() + outputs[0].getCount() <= getStack(0).getMaxCount()
							&& getStack(1).getCount() + outputs[1].getCount() <= getStack(1).getMaxCount()
							&& expStorage >= recipe.experience) {
						if (workTime >= recipe.time) {
							getStack(0).increment(outputs[0].getCount());
							getStack(1).increment(outputs[1].getCount());
							workTime = 0;
							expStorage -= recipe.experience;
						} else {
							workTime++;
						}
					}
				}
			}
			if (!getStack(0).isEmpty()) {
				setStack(0, output(getStack(0)));
			}
			if (!getStack(1).isEmpty()) {
				setStack(1, output(getStack(1)));
			}
		}
	}

	/*
	 * 以下是Inventory的方法
	 */
	@Override
	public int size() {
		return 5;
	}

	@Override
	public void onOpen(PlayerEntity player) {
		propertyDelegate.set(3, propertyDelegate.get(3));
		super.onOpen(player);
	}

	/*
	 * 以下是自定义方法
	 */
	public Degree getTemperature() {
		return temperature;
	}

	public void setTemperature(Degree t) {
		if (availabeTemperature.contains(t))
			temperature = t;
//		propertyDelegate.set(3, propertyDelegate.get(3));
	}

	public Degree getPressure() {
		return pressure;
	}

	public void setPressure(Degree p) {
		if (availabePressure.contains(p))
			pressure = p;
//		propertyDelegate.set(3, propertyDelegate.get(3));
	}

	public void shiftTemperature() {
		System.out.println(temperature);
		propertyDelegate.set(3, propertyDelegate.get(3) + 16);
	}

	public void shiftPressure() {
		System.out.println(pressure);
		propertyDelegate.set(3, propertyDelegate.get(3) + 1);
	}

	public boolean isWorking() {
		return workTime > 0;
	}

	public Recipe getRecipe() {
		return RECIPES.get(getHashCode(temperature, pressure, getStack(0).getItem(), getStack(1).getItem()));
	}

	public ItemStack output(ItemStack outputStack) {
		Inventory inventory = getOutputInventory();
		ItemStack restStack = ItemStack.EMPTY;
		if (inventory != null) {
			Direction direction = getOutputDir();
			if (!this.isInventoryFull(inventory, direction)) {
				restStack = transfer(this, inventory, outputStack, direction);
				if (restStack.isEmpty()) {
					inventory.markDirty();
				}
			}
		}
		return restStack;
	}

	public static <T> Set<T> createSet(T val1, T val2) {
		HashSet<T> set = new HashSet<>(2);
		set.add(val1);
		set.add(val2);
		return set;
	}

	@SafeVarargs
	public static <T> Set<T> createSet(T... vals) {
		HashSet<T> set = new HashSet<>(vals.length);
		for (T val : vals)
			set.add(val);
		return set;
	}

	public static ItemStack ofItem(Item item) {
		if (ITEM_BUFFER.containsKey(item))
			return ITEM_BUFFER.get(item);
		ItemStack rst = new ItemStack(item);
		ITEM_BUFFER.put(item, rst);
		return rst;
	}

	public static int getHashCode(Degree temperature, Degree pressure, Item input1, Item input2) {
		return (input1.hashCode() ^ input2.hashCode()) * 256 + temperature.ordinal() * 16 + pressure.ordinal();
	}

	static {
		RECIPES.put(getHashCode(Degree.ORDINARY, Degree.HIGH, ACACIA_BUTTON, ACACIA_BOAT), new Recipe(Degree.ORDINARY,
				Degree.ORDINARY, ANCIENT_DEBRIS, FLINT, new ItemStack(ANCIENT_DEBRIS, 2), 4, 200));

	}

	public enum Degree {
		ORDINARY, LOW, HIGH
	}

	public static class Recipe {
		public final Degree temperature;
		public final Degree pressure;
		public final Item input1, input2;
		public final ItemStack output1, output2;
		public final float count2Min, count2Max;
		public final int experience, time;

		public Recipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1,
				ItemStack output2, float count2, int experience, int time) {
			this(temperature, pressure, input1, input2, output1, output2, count2, count2 + 1, experience, time);
		}

		public Recipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1, int experience,
				int time) {
			this(temperature, pressure, input1, input2, output1, ItemStack.EMPTY, 0, 0, experience, time);
		}

		public Recipe(Degree temperature, Degree pressure, Item input1, Item input2, ItemStack output1,
				ItemStack output2, float craft2Min, float craft2Max, int experience, int time) {
			this.temperature = temperature;
			this.pressure = pressure;
			this.input1 = input1;
			this.input2 = input2;
			this.output1 = output1;
			this.output2 = output2;
			this.count2Min = craft2Min;
			this.count2Max = craft2Max;
			this.experience = experience;
			this.time = time;
		}

		public ItemStack[] output() {
			ItemStack[] rst = new ItemStack[2];
			rst[0] = output1.copy();
			rst[1] = output2.copy();
			if (output2 != ItemStack.EMPTY) {
				rst[1] = output2.copy();
				if (count2Max == count2Max) {
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
}
