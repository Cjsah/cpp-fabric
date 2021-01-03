package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.cpp.api.IMaterialGetter;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;

@Mixin(Block.class)
public abstract class MixinBlock extends AbstractBlock implements IMaterialGetter {

	public MixinBlock(Settings settings) {
		super(settings);
	}

	@Override
	public Material getMaterial() {
		return material;
	}
}
