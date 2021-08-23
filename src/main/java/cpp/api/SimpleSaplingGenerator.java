package cpp.api;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.Random;

public class SimpleSaplingGenerator extends SaplingGenerator {
	private final Block log, leaves;
	private Block sapling = Blocks.AIR;

	public SimpleSaplingGenerator(Block log, Block leaves) {
		this.log = log;
		this.leaves = leaves;
	}

	public void setSapling(Block sapling) {
		this.sapling = sapling;
	}

	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl) {
		return Feature.TREE.configure((new TreeFeatureConfig.Builder(
				new SimpleBlockStateProvider(log.getDefaultState()),
				new StraightTrunkPlacer(5, 2, 0),
				new SimpleBlockStateProvider(leaves.getDefaultState()),
				new SimpleBlockStateProvider(sapling.getDefaultState()),
				new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
				new TwoLayersFeatureSize(1, 0, 1)
		)).ignoreVines().build());
	}
}
