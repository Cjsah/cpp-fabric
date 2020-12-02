package net.cpp.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.text.Text;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public abstract class AMachineBlockEntity extends LootableContainerBlockEntity
		implements SidedInventory, Tickable, NamedScreenHandlerFactory, IOutputDiractionalBlockEntity {
	protected Direction outputDir = Direction.EAST;

	protected AMachineBlockEntity(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
	}

	/*
	 * 以下是LockableContainerBlockEntity的方法
	 */
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		outputDir = IOutputDiractionalBlockEntity.byteToDir(tag.getByte("outputDir"));
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putByte("outputDir", IOutputDiractionalBlockEntity.dirToByte(outputDir));
		return tag;
	}
	@Override
	public Text getContainerName() {
		return getCustomName() != null ? getCustomName() : getTitle();
	}

	/*
	 * 以下是IOutputDiractional的方法
	 */
	@Override
	public void setOutputDir(Direction dir) {
		outputDir = dir;
	}

	@Override
	public Direction getOutputDir() {
		return outputDir;
	}

	/*
	 * 以下是自定义方法
	 */
	public abstract Text getTitle();
}
