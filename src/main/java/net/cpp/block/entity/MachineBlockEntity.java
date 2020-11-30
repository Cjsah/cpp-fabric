package net.cpp.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;

public class MachineBlockEntity extends LootableContainerBlockEntity {
	public static final Text TITLE = new TranslatableText("block.cpp.machine");

	protected MachineBlockEntity(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
		// TODO 自动生成的构造函数存根
	}

	@Override
	public int size() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	protected Text getContainerName() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		// TODO 自动生成的方法存根
		return null;
	}

}
