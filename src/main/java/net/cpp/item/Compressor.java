package net.cpp.item;

import static net.cpp.api.CodingTool.rayItem;

import net.cpp.init.CppItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Compressor extends Item {
	public Compressor(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack item = user.getStackInHand(hand);
		if (!world.isClient) {
			ItemEntity itemEntity = rayItem(user);
			if (itemEntity != null) {
				Vec3d pos = itemEntity.getPos();
				ItemStack itemStack = itemEntity.getStack();
				if (itemStack != (itemStack = compress(itemStack))) {
					itemEntity.setStack(itemStack);
					((ServerPlayerEntity) user).networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.PLAYERS, pos.getX(), pos.getY(), pos.getZ(), 1, 1));
					((ServerPlayerEntity) user).networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.CRIT, false, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, .2f, .2f, .2f, .02f, 3));
					user.incrementStat(Stats.USED.getOrCreateStat(this));
					return TypedActionResult.success(item);
				}
			}
		}
		return TypedActionResult.pass(item);
	}

	public static ItemStack compress(ItemStack itemStack) {
		ItemStack result = itemStack;
		if (itemStack.getCount() == itemStack.getMaxCount()) {
			if (itemStack.isOf(CppItems.COMPRESSED_ITEM) || itemStack.isOf(Items.EXPERIENCE_BOTTLE)) {
				int multiple = itemStack.getOrCreateTag().getByte("multiple");
				if (multiple < 8) {
					(result = itemStack.copy()).getOrCreateTag().putByte("multiple", (byte) (multiple + 1));
					result.setCount(1);
				}
			} else {
				CompoundTag tag = new CompoundTag();
				itemStack.toTag(tag);
				result = new ItemStack(CppItems.COMPRESSED_ITEM);
				result.getOrCreateTag().put("item", tag);
				result.getOrCreateTag().putByte("multiple", (byte) 1);
			}
		}
		return result;
	}

}
