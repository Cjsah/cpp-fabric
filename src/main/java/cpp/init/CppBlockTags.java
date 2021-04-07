package cpp.init;

import cpp.Craftingpp;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public final class CppBlockTags {
	public static final Tag.Identified<Block> FLOWER_GRASSES = register("flower_grasses");
	public static final Tag.Identified<Block> FLOWER_GRASSES_1 = register("flower_grasses_1");
	public static final Tag.Identified<Block> FLOWER_GRASSES_2 = register("flower_grasses_2");
	public static final Tag.Identified<Block> ORES = register("ores");
	public static final Tag.Identified<Block> EMPTY = register("empty");

	private CppBlockTags() {
	}

	private static Tag.Identified<Block> register(String id) {
		return TagRegistry.create(new Identifier(Craftingpp.MOD_ID3, id), BlockTags::getTagGroup);
	}
}
