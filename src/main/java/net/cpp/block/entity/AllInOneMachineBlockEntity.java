package net.cpp.block.entity;

import net.cpp.Registeror;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

public class AllInOneMachineBlockEntity extends AMachineBlockEntity {
	private int processTime = 0;
	private int expStorage = 0;
	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
	private Temperature temperature = Temperature.ORDINARY;
	private Pressure pressure = Pressure.ORDINARY;
	private final PropertyDelegate propertyDelegate = new PropertyDelegate() {

		@Override
		public int size() {
			return 3;
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
			case 0:
				processTime = value;
			case 1:
				expStorage = value;
			case 2:
				temperature = Temperature.values()[value % 9 / 3];
				pressure = Pressure.values()[value % 3];
			default:
				break;
			}
		}

		@Override
		public int get(int index) {
			switch (index) {
			case 0:
				return processTime;
			case 1:
				return expStorage;
			case 2:
				return temperature.ordinal() * 3 + pressure.ordinal();
			default:
				return -1;
			}
		}
	};

	public AllInOneMachineBlockEntity(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
	}

	/*
	 * 以下是AMachineBlock的方法
	 */
	@Override
	public Text getTitle() {
		return Registeror.ALL_IN_ONE_MACHINE_BLOCK.getName();
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
	 * 以下是LockableContainerBlockEntity的方法（非 Javadoc）
	 */
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		Inventories.fromTag(tag, inventory);
		processTime = tag.getInt("processTime");
		expStorage = tag.getInt("expStorage");
		propertyDelegate.set(2, tag.getByte("temperaturePressure"));
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		Inventories.toTag(tag, inventory);
		tag.putInt("processTime", processTime);
		tag.putInt("expStorage", expStorage);
		tag.putByte("temperaturePressure", (byte) propertyDelegate.get(2));
		return tag;
	}
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		// TODO 自动生成的方法存根
		return null;
	}
	/*
	 * 以下是SidedInventory的方法（非 Javadoc）
	 */
	@Override
	public int[] getAvailableSlots(Direction side) {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		// TODO 自动生成的方法存根
		return false;
	}
	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}
	/*
	 * 以下是Tickable的方法（非 Javadoc）
	 */
	@Override
	public void tick() {
		// TODO 自动生成的方法存根
		
	}
	/*
	 * 以下是Inventory的方法（非 Javadoc）
	 */
	@Override
	public int size() {
		return 5;
	}
	/*
	 * 以下是自定义方法
	 */
	
	public enum Temperature {
		HIGH, ORDINARY, LOW
	}

	public enum Pressure {
		HIGH, ORDINARY, LOW
	}
}
