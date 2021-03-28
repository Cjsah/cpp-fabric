package net.cpp.init;

import net.cpp.Craftingpp;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public final class CppItemTags {
	public static final Tag<Item> FLOWER_GRASSES = register("flower_grasses");
	public static final Tag<Item> FLOWER_GRASSES_1 = register("flower_grasses_1");
	public static final Tag<Item> FLOWER_GRASSES_2 = register("flower_grasses_2");
	public static final Tag<Item> FLOWER_GRASS_SEEDS = register("flower_grass_seeds");
	public static final Tag<Item> FRUITS = register("fruits");
	public static final Tag<Item> DROPPABLE_FRUITS = register("droppable_fruits");
	public static final Tag<Item> RARE_DROPS = register("rare_drops");
	public static final Tag<Item> AGENTIAS = register("agentias");
	public static final Tag<Item> ORES = register("ores");

	private CppItemTags() {
	}

	private static Tag<Item> register(String id) {
		return TagRegistry.item(new Identifier(Craftingpp.MOD_ID3, id));
	}
}
