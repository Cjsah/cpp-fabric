package net.cpp.item;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Magnet extends Item implements IDefaultTagItem {
	public Magnet(Settings settings) {
		super(settings);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		boolean enabled = stack.getOrCreateTag().getBoolean("enabled");
		tooltip.add(new TranslatableText(enabled ? "addServer.resourcePack.enabled" : "addServer.resourcePack.disabled").formatted(enabled ? Formatting.GREEN : Formatting.GRAY));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (!world.isClient) {
			boolean enabled;
			itemStack.getOrCreateTag().putBoolean("enabled", enabled = !itemStack.getOrCreateTag().getBoolean("enabled"));
			((ServerPlayerEntity) user).networkHandler.sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.ACTIONBAR, new TranslatableText("chat.cpp.change", new TranslatableText(enabled ? "addServer.resourcePack.enabled" : "addServer.resourcePack.disabled").formatted(enabled ? Formatting.GREEN : Formatting.GRAY))));
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			return TypedActionResult.success(itemStack);
		}
		return TypedActionResult.pass(itemStack);
	}

	@Override
	public CompoundTag modifyDefaultTag(CompoundTag tag) {
		tag.putBoolean("enabled", true);
		return tag;
	}
}
