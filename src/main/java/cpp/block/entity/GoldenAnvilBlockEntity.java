package cpp.block.entity;

import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import cpp.init.CppBlockEntities;
import cpp.init.CppBlocks;
import cpp.init.CppItems;
import cpp.screen.handler.GoldenAnvilScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cpp.init.CppItems.*;
import static net.minecraft.item.Items.*;

/**
 * 金砧方块实体
 * 
 * @author Ph-苯
 *
 */
public class GoldenAnvilBlockEntity extends AExpMachineBlockEntity {
	private static final int[] AVAILABLE_SLOTS = new int[] { 0, 1 };
	/**
	 * 右原料槽可放入的物品
	 */
	public static final Set<Item> RIGHT_SLOT_INSERTABLE = ImmutableSet.of(CppItems.WIFI_PLUGIN, Items.BOOK, CppItems.MOON_SHARD, Items.ENCHANTED_GOLDEN_APPLE, CppItems.ANCIENT_SCROLL);
	public final PropertyDelegate propertyDelegate = new ExpPropertyDelegate();

	public GoldenAnvilBlockEntity() {
		this(BlockPos.ORIGIN, CppBlocks.GOLDEN_ANVIL.getDefaultState());
	}

	public GoldenAnvilBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(CppBlockEntities.GOLDEN_ANVIL, blockPos, blockState);
		setCapacity(4);
	}

	@Override
	public PropertyDelegate getPropertyDelegate() {
		return propertyDelegate;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, inventory);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, inventory);
		return nbt;
	}

	public static void tick(World world, BlockPos pos, BlockState state, GoldenAnvilBlockEntity blockEntity) {
		if (!world.isClient) {
			blockEntity.transferExpBottle();
			ItemStack leftStack = blockEntity.getStack(1), rightStack = blockEntity.getStack(2);
			boolean empty = blockEntity.getStack(3).isEmpty();
			if (leftStack.getItem() == MENDING_PLUGIN && rightStack.getItem() == WIFI_PLUGIN) {
				try {
					List<ServerPlayerEntity> players = new EntitySelectorReader(new StringReader("@a[distance=..32]")).read().getPlayers(blockEntity.getServerCommandSource());
					for (ServerPlayerEntity player : players) {
						for (ItemStack stack : player.getItemsHand()) {
							tryMend(stack, blockEntity);
						}
						for (ItemStack stack : player.getItemsEquipped()) {
							tryMend(stack, blockEntity);
						}
					}
				} catch (CommandSyntaxException e) {
					e.printStackTrace();
				}
			}
			if (empty) {
				if (leftStack.getItem() == MENDING_PLUGIN && rightStack.isDamageable()) {
					tryMend(rightStack, blockEntity);
					if (!rightStack.isDamaged())
						blockEntity.transferResult(2);
				} else if (rightStack.isOf(BOOK) && blockEntity.expStorage >= 128 && leftStack.hasEnchantments()) {
					ItemStack enchentedBook = ENCHANTED_BOOK.getDefaultStack();
					EnchantmentHelper.set(EnchantmentHelper.get(leftStack), enchentedBook);
					blockEntity.setStack(3, enchentedBook);
					leftStack.removeSubTag("Enchantments");
					rightStack.decrement(1);
					blockEntity.expStorage -= 128;
					blockEntity.output(1);
				} else if (rightStack.isOf(MOON_SHARD) && blockEntity.expStorage >= 256 && leftStack.getRepairCost() > 0) {
					leftStack.setRepairCost(0);
					rightStack.decrement(1);
					blockEntity.expStorage -= 256;
					blockEntity.transferResult(1);
				} else if (rightStack.isOf(ENCHANTED_GOLDEN_APPLE) && blockEntity.expStorage >= 256 && leftStack.isEnchantable()) {
					boolean hasCursed = false;
					Iterator<NbtElement> iterator = leftStack.getEnchantments().iterator();
					while (iterator.hasNext()) {
						NbtElement nbtElement = iterator.next();
						if (nbtElement instanceof NbtCompound) {
							NbtCompound nbt = (NbtCompound) nbtElement;
							String name = nbt.getString("id");
							Enchantment enchantment = Registry.ENCHANTMENT.get(new Identifier(name));
							if (enchantment.isCursed()) {
								hasCursed = true;
								iterator.remove();
							}
						}
					}
					if (hasCursed) {
						rightStack.decrement(1);
						blockEntity.expStorage -= 256;
					}
					blockEntity.transferResult(1);
				} else if (rightStack.isOf(ANCIENT_SCROLL) && blockEntity.expStorage >= 512 && leftStack.hasEnchantments()) {
					Enchantment enchantment = Registry.ENCHANTMENT.get(new Identifier(rightStack.getOrCreateTag().getString("enchantment")));
					if (EnchantmentHelper.getLevel(enchantment, leftStack) == enchantment.getMaxLevel()) {
						Map<Enchantment, Integer> map = EnchantmentHelper.get(leftStack);
						map.compute(enchantment, (e, o) -> o + 1);
						EnchantmentHelper.set(map, leftStack);
						rightStack.decrement(1);
						blockEntity.expStorage -= 512;
						blockEntity.transferResult(1);
					}
				}
			}
			blockEntity.output(3);
		}
	}

	/**
	 * 将{@code getStack(index)}的物品转移到成品槽
	 * 
	 * @param index
	 */
	public void transferResult(int index) {
		setStack(3, getStack(index));
		setStack(index, ItemStack.EMPTY);
	}

	public static void tryMend(ItemStack stack, AExpMachineBlockEntity blockEntity) {
		if (stack.isDamaged() && blockEntity.expStorage > 0) {
			stack.damage(-1, blockEntity.getWorld().random, null);
			blockEntity.expStorage--;
		}
	}

	@Override
	public DefaultedList<ItemStack> getInvStackList() {
		return inventory;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		inventory = list;
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new GoldenAnvilScreenHandler(syncId, playerInventory, this);
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return AVAILABLE_SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		if (stack.getItem() == Items.EXPERIENCE_BOTTLE)
			return slot == 0;
		if (slot == 1)
			return stack.getItem() == CppItems.MENDING_PLUGIN || stack.hasEnchantments();
		if (slot == 2)
			return stack.isDamageable() || RIGHT_SLOT_INSERTABLE.contains(stack.getItem());
		return false;
	}

	static {

	}

}
