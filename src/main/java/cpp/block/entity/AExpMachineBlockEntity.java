package cpp.block.entity;

import cpp.misc.ExperienceBottleHooks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static net.minecraft.item.Items.EXPERIENCE_BOTTLE;

/**
 * 带经验槽和方向输出的机器方块实体
 *
 * @author Ph-苯
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
//		new CppFeatures();
}

@Override
public void readNbt(NbtCompound nbt) {
	super.readNbt(nbt);
	workTime = nbt.getInt("workTime");
	expStorage = nbt.getInt("expStorage");
}

@Override
public NbtCompound writeNbt(NbtCompound nbt) {
	super.writeNbt(nbt);
	nbt.putInt("workTime", workTime);
	nbt.putInt("expStorage", expStorage);
	return nbt;
}

@Override
public boolean canInsert(int slot, ItemStack stack, Direction dir) {
	return slot == 0 && stack.isOf(EXPERIENCE_BOTTLE);
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
	if (getStack(0).isOf(EXPERIENCE_BOTTLE)) {
		int exp = ExperienceBottleHooks.calc(getStack(0));
		if (expStorage + exp <= XP_CAPACITY) {
			getStack(0).decrement(1);
			expStorage += exp;
		}
	}
}

public void tick(World world, BlockPos pos, BlockState state) {
	if (!world.isClient)
		transferExpBottle();
}

/**
 * 同步经验槽
 *
 * @author Ph-苯
 */
protected class ExpPropertyDelegate extends OutputDirectionPropertyDelegate {
	@Override
	public int size() {
		return super.size() + 3;
	}

	@Override
	public void set(int index, int value) {
		switch (index - super.size()) {
			case 0:
				workTime = value;
				break;
			case 1:
				workTimeTotal = value;
				break;
			case 2:
				expStorage = value;
				break;
			default:
				super.set(index, value);
		}
	}

	@Override
	public int get(int index) {
		switch (index - super.size()) {
			case 0:
				return workTime;
			case 1:
				return workTimeTotal;
			case 2:
				return expStorage;
			default:
				return super.get(index);
		}
	}
}
}
