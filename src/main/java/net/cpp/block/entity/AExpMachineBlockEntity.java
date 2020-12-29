package net.cpp.block.entity;

import static net.minecraft.item.Items.EXPERIENCE_BOTTLE;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * 带经验槽和方向输出的机器方块实体
 * 
 * @author Ph-苯
 *
 */
public abstract class AExpMachineBlockEntity extends AOutputMachineBlockEntity {
	/**
	 * 经验槽容量
	 */
	public static final int XP_CAPACITY = 1000;
	protected int workTime;
	protected int workTimeTotal;
	protected int expStorage;

	protected AExpMachineBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		workTime = tag.getInt("workTime");
		expStorage = tag.getInt("expStorage");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putInt("workTime", workTime);
		tag.putInt("expStorage", expStorage);
		return tag;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return slot == 0 && stack.getItem() == EXPERIENCE_BOTTLE;
	}

	public boolean isWorking() {
		return workTime > 0;
	}

	public int getWorkTime() {
		return workTime;
	}

	public int getWorkTimeTotal() {
		return workTimeTotal;
	}

	public void setExpStorage(int expStorage) {
		this.expStorage = expStorage;
	}

	public int getExpStorage() {
		return expStorage;
	}

	/**
	 * 把附魔之瓶槽里的附魔之瓶转化成经验储存到经验槽里
	 */
	protected void transferExpBottle() {
		if (getStack(0).getItem() == EXPERIENCE_BOTTLE && expStorage + 9 <= XP_CAPACITY) {
			getStack(0).decrement(1);
			expStorage += 9;
		}
	}

	/**
	 * 同步了经验槽
	 * 
	 * @author Ph-苯
	 *
	 */
	protected class ExpPropertyDelegate extends OutputDirectionPropertyDelegate {
		@Override
		public int size() {
			return 4;
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
			case 1:
				workTime = value;
				break;
			case 2:
				workTimeTotal = value;
				break;
			case 3:
				expStorage = value;
				break;
			default:
				super.set(index, value);
			}
		}

		@Override
		public int get(int index) {
			switch (index) {
			case 1:
				return workTime;
			case 2:
				return workTimeTotal;
			case 3:
				return expStorage;
			default:
				return super.get(index);
			}
		}
	}
}
