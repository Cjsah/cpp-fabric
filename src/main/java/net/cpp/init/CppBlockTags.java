package net.cpp.init;

import net.cpp.Craftingpp;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public final class CppBlockTags {
	public static final Tag<Block> FLOWER_GRASSES = register("flower_grasses");
	public static final Tag<Block> FLOWER_GRASSES_1 = register("flower_grasses_1");
	public static final Tag<Block> FLOWER_GRASSES_2 = register("flower_grasses_2");
	private CppBlockTags() {
	}

	private static Tag<Block> register(String id) {
		return TagRegistry.block(new Identifier(Craftingpp.MOD_ID3, id));
	}
}
