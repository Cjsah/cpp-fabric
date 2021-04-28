package cpp.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

/**
 * 默认机器
 * 
 * @author Ph-苯
 *
 */
public abstract class AMachineBlockEntity extends LootableContainerBlockEntity {
	protected DefaultedList<ItemStack> inventory = DefaultedList.of();

	public AMachineBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.serializeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.inventory);
		}
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if (!this.deserializeLootTable(nbt)) {
			Inventories.readNbt(nbt, this.inventory);
		}
	}

	public int size() {
		return getInvStackList().size();
	}

	protected DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}

	protected void setInvStackList(DefaultedList<ItemStack> list) {
		this.inventory = list;
	}

	@Override
	public Text getContainerName() {
		return getCustomName() != null ? getCustomName() : new TranslatableText(getCachedState().getBlock().getTranslationKey());
	}

	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);
	}

	public void onOpen(PlayerEntity player) {

	}

	public void onClose(PlayerEntity player) {

	}

	/**
	 * 设置物品栏容量
	 * 
	 * @param capacity 容量
	 * @return 设置后的物品栏
	 */
	protected DefaultedList<ItemStack> setCapacity(int capacity) {
		return inventory = DefaultedList.ofSize(capacity, ItemStack.EMPTY);
	}
}
