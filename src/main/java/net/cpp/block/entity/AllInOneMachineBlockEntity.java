package net.cpp.block.entity;

import java.lang.Enum;
import java.util.Iterator;

import net.cpp.gui.handler.AllInOneMachineScreenHandler;
import net.cpp.gui.handler.CraftingMachineScreenHandler;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppBlocks;
import net.cpp.init.CppRecipes;
import net.cpp.recipe.AllInOneMachineRecipe;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class AllInOneMachineBlockEntity extends AMachineBlockEntity implements RecipeUnlocker, RecipeInputProvider {
	private static final int[] AVAILABLE_SLOTS = new int[] { 0, 1, 2 };
	private int workTime = 0;
	private int expStorage = 0;
	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
	private Temperature temperature = Temperature.ORDINARY;
	private Pressure pressure = Pressure.ORDINARY;
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
				temperature = Temperature.values()[value % 0x100 / 0x10 % Temperature.values().length];
				pressure = Pressure.values()[value % 0x10 % Pressure.values().length];
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
				return temperature.ordinal() * 0x10 + pressure.ordinal();
			default:
				return -1;
			}
		}
	};

	public AllInOneMachineBlockEntity() {
		super(CppBlockEntities.ALL_IN_ONE_MACHINE);
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
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		Inventories.toTag(tag, inventory);
		tag.putInt("processTime", workTime);
		tag.putInt("expStorage", expStorage);
		tag.putByte("temperaturePressure", (byte) propertyDelegate.get(3));
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
	@Override
	public int[] getAvailableSlots(Direction side) {
		return AVAILABLE_SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return slot == 0 && !getStack(1).getItem().equals(stack.getItem())
				|| slot == 1 && !getStack(0).getItem().equals(stack.getItem())
				|| slot == 2 && stack.getItem().equals(Items.EXPERIENCE_BOTTLE);
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}

	/*
	 * 以下是RecipeUnlocker的方法
	 */
	@Override
	public void setLastRecipe(Recipe<?> recipe) {
		if (recipe != null) {
			Identifier identifier = recipe.getId();
			recipesUsed.addTo(identifier, 1);
		}
	}

	@Override
	public Recipe<?> getLastRecipe() {
		return null;
	}

	/*
	 * 以下是RecipeInputProvider的方法
	 */
	@Override
	public void provideRecipeInputs(RecipeFinder finder) {
		Iterator<ItemStack> var2 = this.inventory.iterator();
		while (var2.hasNext()) {
			ItemStack itemStack = (ItemStack) var2.next();
			finder.addItem(itemStack);
		}
	}

	/*
	 * 以下是Tickable的方法
	 */
	@Override
	public void tick() {
//		System.out.println(world + ":" + propertyDelegate.get(3));
//		boolean working = isWorking();
//		boolean dirty = false;
//		if (working)
//			workTime++;
//		if (!this.world.isClient) {
//			ItemStack itemStack = this.inventory.get(2);
//			if (!this.isWorking() && (itemStack.isEmpty() || this.inventory.get(0).isEmpty())) {
//				if (!this.isWorking() && this.workTime > 0) {
//					this.workTime = MathHelper.clamp(this.workTime - 2, 0, this.getWorkTimeTotal());
//				}
//			} else {
//				AllInOneMachineRecipe recipe = this.world.getRecipeManager()
//						.getFirstMatch(CppRecipes.ALL_IN_ONE_MACHINE_RECIPE_TYPE, this, this.world)
//						.orElse((AllInOneMachineRecipe) null);
//				if (!this.isWorking() && this.canAcceptRecipeOutput(recipe)) {
//					if (this.isWorking()) {
//						dirty = true;
//						if (!itemStack.isEmpty()) {
//							Item item = itemStack.getItem();
//							itemStack.decrement(1);
//							if (itemStack.isEmpty()) {
//								Item item2 = item.getRecipeRemainder();
//								this.inventory.set(1, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
//							}
//						}
//					}
//				}
//
//				if (this.isWorking() && this.canAcceptRecipeOutput(recipe)) {
//					++this.workTime;
//					if (this.workTime == this.getWorkTimeTotal()) {
//						this.workTime = 0;
////						this.workTimeTotal = this.getworkTime();
//						this.craftRecipe(recipe);
//						dirty = true;
//					}
//				} else {
//					this.workTime = 0;
//				}
//			}
//
//			if (working != this.isWorking()) {
//				dirty = true;
//				this.world.setBlockState(this.pos, (BlockState) this.world.getBlockState(this.pos)
//						.with(AbstractFurnaceBlock.LIT, this.isWorking()), 3);
//			}
//		}
//
//		if (dirty) {
//			this.markDirty();
//		}
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
	public Temperature getTemperature() {
		return temperature;
	}

	public void setTemperature(Temperature temperature) {
		propertyDelegate.set(3, temperature.ordinal() * 0x10 + pressure.ordinal());
	}

	public Pressure getPressure() {
		return pressure;
	}

	public void setPressure(Pressure pressure) {
		propertyDelegate.set(3, temperature.ordinal() * 0x10 + pressure.ordinal());
	}

	public void shiftTemperature() {
		propertyDelegate.set(3, propertyDelegate.get(3) + 16);
	}

	public void shiftPressure() {
		propertyDelegate.set(3, propertyDelegate.get(3) + 1);
	}

	public boolean isWorking() {
		return workTime > 0;
	}

	public int getWorkTimeTotal() {
		return 0;// TODO
	}

	protected boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe) {
		if (!this.inventory.get(0).isEmpty() && inventory.get(1).isEmpty() && recipe != null) {
			ItemStack itemStack = recipe.getOutput();
			if (itemStack.isEmpty()) {
				return false;
			} else {
				ItemStack itemStack2 = this.inventory.get(2);
				if (itemStack2.isEmpty()) {
					return true;
				} else if (!itemStack2.isItemEqualIgnoreDamage(itemStack)) {
					return false;
				} else if (itemStack2.getCount() < this.getMaxCountPerStack()
						&& itemStack2.getCount() < itemStack2.getMaxCount()) {
					return true;
				} else {
					return itemStack2.getCount() < itemStack.getMaxCount();
				}
			}
		} else {
			return false;
		}
	}

	private void craftRecipe(@Nullable Recipe<?> recipe) {
		if (recipe != null && this.canAcceptRecipeOutput(recipe)) {
			ItemStack itemStack = this.inventory.get(0);
			ItemStack itemStack2 = recipe.getOutput();
			ItemStack itemStack3 = this.inventory.get(2);
			if (itemStack3.isEmpty()) {
				this.inventory.set(2, itemStack2.copy());
			} else if (itemStack3.getItem() == itemStack2.getItem()) {
				itemStack3.increment(1);
			}

			if (itemStack.getItem() == Blocks.WET_SPONGE.asItem() && !this.inventory.get(1).isEmpty()
					&& this.inventory.get(1).getItem() == Items.BUCKET) {
				this.inventory.set(1, new ItemStack(Items.WATER_BUCKET));
			}

			itemStack.decrement(1);
		}
	}

	public enum Temperature {
		ORDINARY, LOW, HIGH
	}

	public enum Pressure {
		ORDINARY, LOW, HIGH
	}
}
