package net.cpp.api;

import java.util.*;

import javax.annotation.Nullable;

import net.minecraft.block.Blocks;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CodingTool {
	private CodingTool() {

	}

	private static int syncId;

	public static int nextSyncId() {
		return syncId++;
	}

	public static int x(int m) {
		return 8 + m * 18;
	}

	public static int y(int n) {
		return 18 + n * 18;
	}

	/**
	 * 将UUID转化为包含4个元素的int型数组</br>
	 * （本类未使用）
	 *
	 * @author Phoupraw
	 * @see CodingTool#intArrayToUUID(IntArrayTag)
	 * @param uuid 要转化为数组的uuid
	 * @return 转化的数组
	 */
	public static int[] uuidToIntArray(UUID uuid) {
		int[] arr = new int[4];
		arr[0] = (int) (uuid.getMostSignificantBits() >> 32);
		arr[1] = (int) uuid.getMostSignificantBits();
		arr[2] = (int) (uuid.getLeastSignificantBits() >> 32);
		arr[3] = (int) uuid.getLeastSignificantBits();
		return arr;
	}

	/**
	 * 将长为4的int数组标签转化为UUID</br>
	 * （本类未使用）
	 *
	 * @author Phoupraw
	 * @see #uuidToIntArray(UUID)
	 * @param uuidListTag 含有uuid的tag
	 * @return 转化的UUID
	 */
	public static UUID intArrayToUUID(IntArrayTag uuidListTag) {
		long mostSigBits = uuidListTag.get(0).getLong() << 32;
		mostSigBits += uuidListTag.get(1).getLong();
		long leastSigBits = uuidListTag.get(2).getLong() << 32;
		leastSigBits += uuidListTag.get(3).getLong();
		return new UUID(mostSigBits, leastSigBits);
	}

	/**
	 * 获取某个玩家的经验值
	 *
	 * @author Cjsah
	 * @param player 被获取经验值的玩家
	 * @return 获取到的经验值
	 */
	public static int getExperience(PlayerEntity player) {
		return Math.round(player.experienceProgress * player.getNextLevelExperience());
	}

	/**
	 * 获取玩家指向的物品实体
	 *
	 * @author Cjsah
	 * @param player 玩家
	 * @return 物品实体
	 */
	public static ItemEntity rayItem(PlayerEntity player) {
		float length = 0.05F;
		Vec3d playerPos = player.getCameraPosVec(1.0F);
		Vec3d pos = playerPos;
		float yaw = player.yaw;
		float pitch = player.pitch;
		float y = -MathHelper.sin(pitch * (float) (Math.PI) / 180F) * length;
		float x = -MathHelper.sin(yaw * (float) (Math.PI) / 180F);
		float z = MathHelper.cos(yaw * (float) (Math.PI) / 180F);
		float proportion = MathHelper.sqrt((((length * length) - (y * y)) / ((x * x) + (z * z))));
		x *= proportion;
		z *= proportion;
		while (Math.sqrt(Math.pow(pos.x - playerPos.x, 2) + Math.pow(pos.y - playerPos.y, 2) + Math.pow(pos.z - playerPos.z, 2)) < 5) {
			pos = pos.add(x, y, z);
			if (player.world.getBlockState(new BlockPos(pos)).getBlock() != Blocks.AIR) {
				return null;
			}
			Box box = new Box(pos.x - 0.005, pos.y-0.2, pos.z - 0.005, pos.x + 0.005, pos.y + 0.005, pos.z + 0.005);
			List<ItemEntity> list = player.world.getEntitiesByClass(ItemEntity.class, box, (entity) -> entity != null && entity.isAlive());
			if (!list.isEmpty()) {
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * 使某坐标向玩家的前方移动一段距离
	 *
	 * @author Cjsah
	 * @param pos 位移前坐标
	 * @param length 位移长度
	 * @return 位移后的坐标
	 */
	public static Vec3d move(PlayerEntity player, Vec3d pos, float length) {
		float yaw = player.yaw;
		float x = -MathHelper.sin(yaw * (float) (Math.PI) / 180F) * length;
		float z = MathHelper.cos(yaw * (float) (Math.PI) / 180F) * length;
		return pos.add(x, 0, z);
	}

	@SafeVarargs
	public static <E> Set<E> setOf(E... es) {
		return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(es)));
	}

	public static <K, V> Map<K, V> of(K k1, V v1) {
		Map<K, V> rst = new HashMap<>();
		rst.put(k1, v1);
		return Collections.unmodifiableMap(rst);
	}

	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
		Map<K, V> rst = new HashMap<>();
		rst.put(k1, v1);
		rst.put(k2, v2);
		return Collections.unmodifiableMap(rst);
	}

	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
		Map<K, V> rst = new HashMap<>();
		rst.put(k1, v1);
		rst.put(k2, v2);
		rst.put(k3, v3);
		return Collections.unmodifiableMap(rst);
	}

	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		Map<K, V> rst = new HashMap<>();
		rst.put(k1, v1);
		rst.put(k2, v2);
		rst.put(k3, v3);
		rst.put(k4, v4);
		return Collections.unmodifiableMap(rst);
	}

	public static ItemStack newItemStack(Item item, int count, @Nullable CompoundTag nbt) {
		ItemStack rst = new ItemStack(item, count);
		if (nbt != null) {
			rst.setTag(nbt);
		}
		return rst;
	}
	public static void attractItems(Vec3d pos, World world) {
		for (ItemEntity itemEntity : world.getEntitiesByType(EntityType.ITEM, new Box(pos, pos).expand(16), itemEntity -> pos.isInRange(itemEntity.getPos(), 16) && !itemEntity.cannotPickup())) {
			Vec3d v = pos.subtract(itemEntity.getPos());
			double d = pos.distanceTo(itemEntity.getPos());
			if (d < 1)
				v.multiply(d);
			else
				v = v.multiply(1 / v.length());
			itemEntity.setVelocity(v);
		}
	}
}
