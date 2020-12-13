package net.cpp.block.entity;

public class Pair<K, V> {
//	private static final Map<Long, Pair> BUFFER = new WeakHashMap<Long, Pair>();
	public final K e1;
	public final V e2;

	private Pair(K e1, V e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	public static <K, V> Pair<K, V> of(K e1, V e2) {
//		long l = (long)Objects.hashCode(e1) << 32 | Objects.hashCode(e2);
//		if (BUFFER.containsKey(l))
//			return BUFFER.get(l);
//		Pair<K, V> pair = new Pair<K, V>(e1, e2);
//		BUFFER.put(l, pair);
//		return pair;

		return new Pair<K, V>(e1, e2);
	}
}