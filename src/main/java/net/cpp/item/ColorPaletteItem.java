package net.cpp.item;

import static net.cpp.Craftingpp.CPP_GROUP_TOOL;
import static net.cpp.api.CppChat.say;

import net.cpp.gui.handler.ColorPaletteScreenHandler;
import net.cpp.gui.handler.PortableCraftingMachineScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.Settings;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ColorPaletteItem extends Item {
	public ColorPaletteItem(Settings settings) {
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
