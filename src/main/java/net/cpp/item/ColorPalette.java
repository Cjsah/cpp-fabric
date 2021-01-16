package net.cpp.item;

import net.cpp.screen.handler.ColorPaletteScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ColorPalette extends Item {
	public ColorPalette(Settings settings) {
		super(settings);
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack item = user.getStackInHand(hand);
		if (!user.isSneaking()) {
			if (!world.isClient()) {
				user.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new ColorPaletteScreenHandler(i, playerInventory), item.getName()));
			} else {
//				user.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
			}
		}
		return TypedActionResult.pass(item);
	}
}
