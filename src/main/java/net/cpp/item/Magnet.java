package net.cpp.item;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Magnet extends Item {
	public Magnet(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		boolean enabled = stack.getOrCreateTag().getBoolean("enabled");
		tooltip.add(new TranslatableText(enabled ? "addServer.resourcePack.enabled" : "addServer.resourcePack.disabled").formatted(enabled ? Formatting.GREEN : Formatting.GRAY));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (!world.isClient) {
			boolean enabled;
			itemStack.getOrCreateTag().putBoolean("enabled", enabled = !itemStack.getOrCreateTag().getBoolean("enabled"));
			((ServerPlayerEntity) user).networkHandler.sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.ACTIONBAR, new TranslatableText("chat.cpp.gfow.change", new TranslatableText(enabled ? "addServer.resourcePack.enabled" : "addServer.resourcePack.disabled").formatted(enabled ? Formatting.GREEN : Formatting.GRAY))));
		}
		return TypedActionResult.pass(itemStack);
	}
}
