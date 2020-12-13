package net.cpp.item;

import net.cpp.gui.handler.PortableCraftingTableScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PortableCraftingTable extends Item {
	public PortableCraftingTable(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		final ItemStack stack = user.getStackInHand(hand);

		if (!user.isSneaking()) {
			if (!world.isClient()) {
				user.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new PortableCraftingTableScreenHandler(i, playerInventory , ScreenHandlerContext.create(world, new BlockPos(user.getPos())) ), new TranslatableText("container.crafting")));
			} else {
				user.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
			}

			return TypedActionResult.consume(stack);
		}
		return super.use(world, user, hand);
	}
}
