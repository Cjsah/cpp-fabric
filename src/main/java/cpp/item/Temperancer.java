package cpp.item;

import cpp.ducktyping.ITemperancable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Temperancer extends Item {
	public Temperancer(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack item = user.getStackInHand(hand);
		if (!world.isClient) {
			boolean open = !((ITemperancable) user).isEffectEnabled();
			((ITemperancable) user).setEffectEnabled(open);
			user.sendMessage(new TranslatableText("chat.cpp.effect", new TranslatableText(open ? "addServer.resourcePack.enabled" : "addServer.resourcePack.disabled").formatted(open ? Formatting.GREEN : Formatting.GRAY)), true);
		}
		return TypedActionResult.success(item);
	}
}
