package net.cpp.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public abstract class ABarrelLikeBlockEntity extends LootableContainerBlockEntity {
	protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);

	public ABarrelLikeBlockEntity(BlockEntityType<?> blockEntityType,BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		if (!this.serializeLootTable(tag)) {
			Inventories.toTag(tag, this.inventory);
		}
		return tag;
	}

	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if (!this.deserializeLootTable(tag)) {
			Inventories.fromTag(tag, this.inventory);
		}
	}

	public int size() {
		return 27;
	}

	protected DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}

	protected void setInvStackList(DefaultedList<ItemStack> list) {
		this.inventory = list;
	}

	@Override
	public Text getContainerName() {
		return getCustomName() != null ? getCustomName() : getCachedState().getBlock().getName();
	}

	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);
	}

	public void onOpen(PlayerEntity player) {

	}

	public void onClose(PlayerEntity player) {

	}

}