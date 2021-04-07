package net.cpp.item;

import java.util.List;

import net.cpp.api.Utils;
import net.cpp.api.IDefaultTagItem;
import net.cpp.ducktyping.ITickableInItemFrame;
import net.cpp.init.CppItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class Magnet extends Item implements IDefaultTagItem, ITickableInItemFrame {
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

	public static boolean isEnabled(ItemStack stack) {
		return stack.getOrCreateTag().getBoolean("enabled");
	}

	@Override
	public boolean tick(ItemFrameEntity itemFrameEntity) {
		if (itemFrameEntity.getHeldItemStack().getOrCreateTag().getBoolean("enabled") ^ ((itemFrameEntity.getRotation() & 1) != 0)) {
			Utils.attractItems(itemFrameEntity.getPos(), (ServerWorld) itemFrameEntity.world, true, true);
			return true;
		}
		return false;
	}

	public static boolean tick(ServerPlayerEntity player) {
		for (int i = 0; i < player.getInventory().size(); i++) {
			ItemStack itemStack = player.getInventory().getStack(i);
			if (itemStack.isOf(CppItems.MAGNET) && Magnet.isEnabled(itemStack)) {
				Utils.attractItems(player.getPos().add(0, 1, 0), (ServerWorld) player.world, true, false);
				for (ExperienceOrbEntity orb : player.world.getEntitiesByClass(ExperienceOrbEntity.class, new Box(player.getPos(), player.getPos()).expand(16), orb -> orb.getPos().isInRange(player.getPos(), 16))) {
					orb.teleport(player.getPos().x, player.getPos().y, player.getPos().z);
				}
				return true;
			}
		}
		return false;
	}
}
