package cpp.screen.handler;

import static cpp.init.CppItems.ANCIENT_SCROLL;
import static cpp.init.CppItems.MENDING_PLUGIN;
import static cpp.init.CppItems.MOON_SHARD;
import static cpp.init.CppItems.WIFI_PLUGIN;
import static net.minecraft.item.Items.BOOK;
import static net.minecraft.item.Items.ENCHANTED_GOLDEN_APPLE;
import static net.minecraft.item.Items.EXPERIENCE_BOTTLE;

import cpp.api.Utils;
import cpp.block.entity.GoldenAnvilBlockEntity;
import cpp.init.CppScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class GoldenAnvilScreenHandler extends AExpMachineScreenHandler {
	public final GoldenAnvilBlockEntity blockEntity;

	public GoldenAnvilScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new GoldenAnvilBlockEntity(playerInventory.player.getBlockPos(), playerInventory.player.getBlockState()));
	}

	public GoldenAnvilScreenHandler(int syncId, PlayerInventory playerInventory, GoldenAnvilBlockEntity blockEntity) {
		super(CppScreenHandler.GOLDEN_ANVIL, syncId, playerInventory, blockEntity);
		this.blockEntity = blockEntity;

		addSlot(new Slot(blockEntity, 1, Utils.x(1), Utils.y(0)));
		addSlot(new Slot(blockEntity, 2, Utils.x(3), Utils.y(0)));
		addSlot(new ResultSlot(blockEntity, 3, Utils.x(2), Utils.y(2)));
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack = slot.getStack();
			if (index >= 36) {
				this.insertItem(itemStack, 0, 36, true);
			} else if (index < 36) {
				if (itemStack.isOf(EXPERIENCE_BOTTLE))
					insertItem(itemStack, 36);
				else if (itemStack.isOf(MENDING_PLUGIN))
					insertItem(itemStack, 37);
				else if (itemStack.isOf(WIFI_PLUGIN) || itemStack.isOf(BOOK) || itemStack.isOf(ENCHANTED_GOLDEN_APPLE) || itemStack.isOf(MOON_SHARD) || itemStack.isOf(ANCIENT_SCROLL))
					insertItem(itemStack, 38);
				else if (itemStack.isDamaged())
					insertItem(itemStack, 38);
				else if (itemStack.hasEnchantments())
					insertItem(itemStack, 37);
			}
		}
		return ItemStack.EMPTY;
	}
}
