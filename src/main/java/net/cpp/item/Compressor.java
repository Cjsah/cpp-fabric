package net.cpp.item;

import static net.cpp.api.Utils.rayItem;

import net.cpp.init.CppItems;
import net.minecraft.entity.EntityType;
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
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			ItemEntity itemEntity = rayItem(user);
			if (itemEntity != null) {
				Vec3d pos = itemEntity.getPos();
				ItemStack itemStack = itemEntity.getStack();
				if (itemStack != (itemStack = compress(itemStack))) {
					itemEntity.setStack(itemStack);
					itemEntity.setPickupDelay(0);
					((ServerPlayerEntity) user).networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.PLAYERS, pos.getX(), pos.getY(), pos.getZ(), 1, 1));
					((ServerPlayerEntity) user).networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.CRIT, false, pos.getX(), pos.getY()+EntityType.ITEM.getHeight()/2, pos.getZ(), .3f, .3f, .3f, .1f, 10));
					return TypedActionResult.success(item);
				}
			}
		}
		return TypedActionResult.success(item);
	}

	/**
	 * 压缩物品
	 * 
	 * @param itemStack 要被压缩的物品叠
	 * @return 压缩之后的物品，如果物品叠不可压缩，则原样返回（同一个对象）
	 */
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
