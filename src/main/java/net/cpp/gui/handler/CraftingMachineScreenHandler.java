package net.cpp.gui.handler;

import net.cpp.block.entity.CraftingMachineBlockEntity;
import net.cpp.gui.screen.CraftingMachineScreen;
import net.cpp.init.CppScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

/**
 * 合成器GUI
 *
 * @author Ph-苯
 *
 */
public class CraftingMachineScreenHandler extends AbstractRecipeScreenHandler<Inventory> {
    public CraftingMachineScreen screen;
    private ScreenHandlerContext context;
    private PlayerEntity player;
    private World world;
    private CraftingMachineBlockEntity blockEntity;
    private CraftingInventory inputUI;
    private CraftingResultInventory resultUI = new CraftingResultInventory();
    public final PropertyDelegate propertyDelegate;

    public CraftingMachineScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new CraftingMachineBlockEntity(), new ArrayPropertyDelegate(1),
                ScreenHandlerContext.EMPTY);
    }

    public CraftingMachineScreenHandler(int syncId, PlayerInventory playerInventory,
                                        CraftingMachineBlockEntity blockEntity, PropertyDelegate propertyDelegate, ScreenHandlerContext context) {
        super(CppScreenHandler.CRAFTING_MACHINE, syncId);
        player = playerInventory.player;
        world = player.world;
        this.blockEntity = blockEntity;
        inputUI = blockEntity.getInputInventory();
        this.propertyDelegate = propertyDelegate;
        this.context = context;

        addSlot(new CppCraftingResultSlot(player, inputUI, resultUI, 0, 124, 35));
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 3; ++l) {
                this.addSlot(new Slot(inputUI, l + m * 3, 30 + l * 18, 17 + m * 18));
            }
        }
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(player.inventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        for (int m = 0; m < 9; ++m) {
            this.addSlot(new Slot(player.inventory, m, 8 + m * 18, 142));
        }
        blockEntity.onOpen(player);
        updateResultUI();
        addProperties(propertyDelegate);
    }

    /*
     * 以下是AbstractRecipeScreenHandler的方法
     */
    @Override
    public void populateRecipeFinder(RecipeFinder finder) {
        inputUI.provideRecipeInputs(finder);
    }

    @Override
    public void clearCraftingSlots() {
        inputUI.clear();
        resultUI.clear();
    }

    @Override
    public boolean matches(Recipe<? super Inventory> recipe) {
        return recipe.matches(inputUI, world);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 0;
    }

    @Override
    public int getCraftingWidth() {
        return blockEntity.getInputInventory().getWidth();
    }

    @Override
    public int getCraftingHeight() {
        return blockEntity.getInputInventory().getHeight();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int getCraftingSlotCount() {
        return 10;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public RecipeBookCategory getCategory() {
        throw new UnsupportedOperationException("合成器配方没有配方书页面");
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return blockEntity.canPlayerUse(player);
    }

    /*
     * 以下是ScreenHandler的方法
     */

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id == 1010) {
            blockEntity.shiftOutputDir();
            return true;
        }
        return false;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 0) {
                this.context.run((world, blockPos) -> {
                    itemStack2.getItem().onCraft(itemStack2, world, player);
                });
                if (!this.insertItem(itemStack2, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onStackChanged(itemStack2, itemStack);
            } else if (index >= 10 && index < 46) {
                if (!this.insertItem(itemStack2, 1, 10, false)) {
                    if (index < 37) {
                        if (!this.insertItem(itemStack2, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(itemStack2, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.insertItem(itemStack2, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemStack3 = slot.onTakeItem(player, itemStack2);
            if (index == 0) {
                player.dropItem(itemStack3, false);
            }
        }

        return itemStack;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        blockEntity.onClose(player);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run((world2, blockPos) -> {
            updateResultUI();
        });
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.resultUI && super.canInsertIntoSlot(stack, slot);
    }
    /*
	 * 以下是自定义方法
	 */
    private void updateResultUI() {
        if (player instanceof ServerPlayerEntity) {
            resultUI.setStack(0, blockEntity.getResult());
            ((ServerPlayerEntity) player).networkHandler
                    .sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 0, resultUI.getStack(0)));
        }
    }
}
