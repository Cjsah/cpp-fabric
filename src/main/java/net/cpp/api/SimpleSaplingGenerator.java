package net.cpp.api;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

public class SimpleSaplingGenerator extends SaplingGenerator {
	public final Block log, leaves;

	public SimpleSaplingGenerator(Block log, Block leaves) {
		this.log = log;
		this.leaves = leaves;
	}

	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl) {
		return Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(log.getDefaultState()), new SimpleBlockStateProvider(leaves.getDefaultState()), new BlobFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 3), new StraightTrunkPlacer(5, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
	}

	static {

	}
}
