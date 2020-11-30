package net.cpp.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
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
	 * 以下是LockableContainerBlockEntity的方法（非 Javadoc）
	 */
	@Override
	public Text getContainerName() {
		return getCustomName() != null ? getCustomName() : getTitle();
	}

	/*
	 * 以下是IOutputDiractional的方法（非 Javadoc）
	 */
	@Override
	public Direction setOutputDir(Direction dir) {
		Direction preDir = outputDir;
		outputDir = dir;
		return preDir;
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
