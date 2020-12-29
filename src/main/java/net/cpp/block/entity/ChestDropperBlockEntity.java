package net.cpp.block.entity;

import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
/**
 * 箱漏方块实体
 * @author Ph-苯
 *
 */
public class ChestDropperBlockEntity extends AMachineBlockEntity {

	public ChestDropperBlockEntity(BlockPos pos, BlockState state) {
		super(CppBlockEntities.CHEST_DROPPER, pos, state);
		setCapacity(27);
	}
/**
 * 获取物品栏中第一个物品叠
 * @return 物品栏中第一个物品叠
 */
	public ItemStack getFirstStack() {
		ItemStack stack = ItemStack.EMPTY;
		for (int i = 0; i < size(); i++)
			if (!getStack(i).isEmpty()) {
				stack = getStack(i);
				break;
			}
		return stack;
	}

	public static void tick(World world, BlockPos pos, BlockState state, ChestDropperBlockEntity blockEntity) {
		if (!world.isClient && world.isReceivingRedstonePower(pos) && !blockEntity.isEmpty()) {
			ItemStack stack = blockEntity.getFirstStack(), stack2 = stack.copy();
			stack2.setCount(1);
			stack.decrement(1);
			ItemEntity itemEntity = new ItemEntity(world, pos.getX() + .5, pos.getY() - EntityType.ITEM.getHeight(), pos.getZ() + .5, stack2);
			itemEntity.setVelocity(0, 0, 0);
			world.spawnEntity(itemEntity);
		}
	}
}
