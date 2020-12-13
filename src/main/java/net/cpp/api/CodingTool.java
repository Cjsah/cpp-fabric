package net.cpp.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.nbt.IntArrayTag;

public class CodingTool {
	private CodingTool() {
		
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
	 * @see CodingTool#intArrayToUUID(IntArrayTag)
	 * @param uuid
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
	 * @see #uuidToIntArray(UUID)
	 * @param uuidListTag
	 * @return 转化的UUID
	 */
	public static UUID intArrayToUUID(IntArrayTag uuidListTag) {
		long mostSigBits = uuidListTag.get(0).getLong() << 32;
		mostSigBits += uuidListTag.get(1).getLong();
		long leastSigBits = uuidListTag.get(2).getLong() << 32;
		leastSigBits += uuidListTag.get(3).getLong();
		return new UUID(mostSigBits, leastSigBits);
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
}
