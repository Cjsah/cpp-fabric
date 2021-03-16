package net.cpp.island;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cpp.Craftingpp;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.FillLayerFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class IslandChunkGeneratorConfig implements HeightLimitView {
    private static final Logger LOGGER = Craftingpp.logger;
    public static final Codec<IslandChunkGeneratorConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter((flatChunkGeneratorConfig) -> {
            return flatChunkGeneratorConfig.biomeRegistry;
        }), StructuresConfig.CODEC.fieldOf("structures").forGetter(IslandChunkGeneratorConfig::getStructuresConfig), FlatChunkGeneratorLayer.CODEC.listOf().fieldOf("layers").forGetter(IslandChunkGeneratorConfig::getLayers), Codec.BOOL.fieldOf("lakes").orElse(false).forGetter((flatChunkGeneratorConfig) -> {
            return flatChunkGeneratorConfig.hasLakes;
        }), Codec.BOOL.fieldOf("features").orElse(false).forGetter((flatChunkGeneratorConfig) -> {
            return flatChunkGeneratorConfig.hasFeatures;
        }), Biome.REGISTRY_CODEC.optionalFieldOf("biome").orElseGet(Optional::empty).forGetter((flatChunkGeneratorConfig) -> {
            return Optional.of(flatChunkGeneratorConfig.biome);
        })).apply(instance, IslandChunkGeneratorConfig::new);
    });
    private static final Map<StructureFeature<?>, ConfiguredStructureFeature<?, ?>> STRUCTURE_TO_FEATURES = Util.make(Maps.newHashMap(), (hashMap) -> {
        hashMap.put(StructureFeature.MINESHAFT, ConfiguredStructureFeatures.MINESHAFT);
        hashMap.put(StructureFeature.VILLAGE, ConfiguredStructureFeatures.VILLAGE_PLAINS);
        hashMap.put(StructureFeature.STRONGHOLD, ConfiguredStructureFeatures.STRONGHOLD);
        hashMap.put(StructureFeature.SWAMP_HUT, ConfiguredStructureFeatures.SWAMP_HUT);
        hashMap.put(StructureFeature.DESERT_PYRAMID, ConfiguredStructureFeatures.DESERT_PYRAMID);
        hashMap.put(StructureFeature.JUNGLE_PYRAMID, ConfiguredStructureFeatures.JUNGLE_PYRAMID);
        hashMap.put(StructureFeature.IGLOO, ConfiguredStructureFeatures.IGLOO);
        hashMap.put(StructureFeature.OCEAN_RUIN, ConfiguredStructureFeatures.OCEAN_RUIN_COLD);
        hashMap.put(StructureFeature.SHIPWRECK, ConfiguredStructureFeatures.SHIPWRECK);
        hashMap.put(StructureFeature.MONUMENT, ConfiguredStructureFeatures.MONUMENT);
        hashMap.put(StructureFeature.END_CITY, ConfiguredStructureFeatures.END_CITY);
        hashMap.put(StructureFeature.MANSION, ConfiguredStructureFeatures.MANSION);
        hashMap.put(StructureFeature.FORTRESS, ConfiguredStructureFeatures.FORTRESS);
        hashMap.put(StructureFeature.PILLAGER_OUTPOST, ConfiguredStructureFeatures.PILLAGER_OUTPOST);
        hashMap.put(StructureFeature.RUINED_PORTAL, ConfiguredStructureFeatures.RUINED_PORTAL);
        hashMap.put(StructureFeature.BASTION_REMNANT, ConfiguredStructureFeatures.BASTION_REMNANT);
    });

    private final Registry<Biome> biomeRegistry;
    private final StructuresConfig structuresConfig;
    private final List<FlatChunkGeneratorLayer> layers;
    private Supplier<Biome> biome;
    private final BlockState[] layerBlocks;
    private boolean hasNoTerrain;
    private boolean hasFeatures;
    private boolean hasLakes;

    public IslandChunkGeneratorConfig(Registry<Biome> biomeRegistry, StructuresConfig structuresConfig, List<FlatChunkGeneratorLayer> layers, boolean hasLakes, boolean hasFeatures, Optional<Supplier<Biome>> biome) {
        // 多群系
//        this.biomeSource = new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry);
        // 单群系
//        this.biomeSource = new FixedBiomeSource(biomeRegistry.getOrThrow(BiomeKeys.PLAINS));
        this(structuresConfig, biomeRegistry);
        if (hasLakes) {
            this.enableLakes();
        }

        if (hasFeatures) {
            this.enableFeatures();
        }

        this.layers.addAll(layers);
        this.updateLayerBlocks();
        if (!biome.isPresent()) {
            LOGGER.error("Unknown biome, defaulting to plains");
            this.biome = () -> {
                return (Biome)biomeRegistry.getOrThrow(BiomeKeys.PLAINS);
            };
        } else {
            this.biome = biome.get();
        }


    }

    public IslandChunkGeneratorConfig(StructuresConfig structuresConfig, Registry<Biome> biomeRegistry) {
        this.layers = Lists.newArrayList();
        this.biomeRegistry = biomeRegistry;
        this.structuresConfig = structuresConfig;
        this.biome = () -> {
            return (Biome)biomeRegistry.getOrThrow(BiomeKeys.PLAINS);
        };
        this.layerBlocks = new BlockState[this.getHeight()];
    }

    public void enableFeatures() {
        this.hasFeatures = true;
    }

    public void enableLakes() {
        this.hasLakes = true;
    }

    public Biome createBiome() {
        Biome biome = this.getBiome();
        GenerationSettings generationSettings = biome.getGenerationSettings();
        GenerationSettings.Builder builder = (new GenerationSettings.Builder()).surfaceBuilder(generationSettings.getSurfaceBuilder());
        if (this.hasLakes) {
            builder.feature(GenerationStep.Feature.LAKES, ConfiguredFeatures.LAKE_WATER);
            builder.feature(GenerationStep.Feature.LAKES, ConfiguredFeatures.LAKE_LAVA);
        }

        Iterator var4 = this.structuresConfig.getStructures().entrySet().iterator();

        while(var4.hasNext()) {
            Map.Entry<StructureFeature<?>, StructureConfig> entry = (Map.Entry)var4.next();
            builder.structureFeature(generationSettings.method_30978((ConfiguredStructureFeature)STRUCTURE_TO_FEATURES.get(entry.getKey())));
        }

        boolean bl = (!this.hasNoTerrain || this.biomeRegistry.getKey(biome).equals(Optional.of(BiomeKeys.THE_VOID))) && this.hasFeatures;
        int i;
        if (bl) {
            List<List<Supplier<ConfiguredFeature<?, ?>>>> list = generationSettings.getFeatures();

            for(i = 0; i < list.size(); ++i) {
                if (i != GenerationStep.Feature.UNDERGROUND_STRUCTURES.ordinal() && i != GenerationStep.Feature.SURFACE_STRUCTURES.ordinal()) {
                    List<Supplier<ConfiguredFeature<?, ?>>> list2 = (List)list.get(i);
                    Iterator var8 = list2.iterator();

                    while(var8.hasNext()) {
                        Supplier<ConfiguredFeature<?, ?>> supplier = (Supplier)var8.next();
                        builder.feature(i, supplier);
                    }
                }
            }
        }

        BlockState[] blockStates = this.getLayerBlocks();

        for(i = 0; i < blockStates.length; ++i) {
            BlockState blockState = blockStates[i];
            if (blockState != null && !Heightmap.Type.MOTION_BLOCKING.getBlockPredicate().test(blockState)) {
                this.layerBlocks[i] = null;
                int k = this.getBottomY() + i;
                builder.feature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, net.minecraft.world.gen.feature.Feature.FILL_LAYER.configure(new FillLayerFeatureConfig(k, blockState)));
            }
        }

        return (new net.minecraft.world.biome.Biome.Builder()).precipitation(biome.getPrecipitation()).category(biome.getCategory()).depth(biome.getDepth()).scale(biome.getScale()).temperature(biome.getTemperature()).downfall(biome.getDownfall()).effects(biome.getEffects()).generationSettings(builder.build()).spawnSettings(biome.getSpawnSettings()).build();
    }
    @Environment(EnvType.CLIENT)
    public void setBiome(Supplier<Biome> biome) {
        this.biome = biome;
    }

    public List<FlatChunkGeneratorLayer> getLayers() {
        return this.layers;
    }

    public BlockState[] getLayerBlocks() {
        return this.layerBlocks;
    }

    public void updateLayerBlocks() {
        Arrays.fill(this.layerBlocks, 0, this.layerBlocks.length, (Object)null);
        int i = this.getBottomY();

        FlatChunkGeneratorLayer flatChunkGeneratorLayer;
        for(Iterator var2 = this.layers.iterator(); var2.hasNext(); i += flatChunkGeneratorLayer.getThickness()) {
            flatChunkGeneratorLayer = (FlatChunkGeneratorLayer)var2.next();
            flatChunkGeneratorLayer.setStartY(i);
        }

        this.hasNoTerrain = true;
        Iterator var5 = this.layers.iterator();

        while(var5.hasNext()) {
            FlatChunkGeneratorLayer flatChunkGeneratorLayer2 = (FlatChunkGeneratorLayer)var5.next();

            for(int j = flatChunkGeneratorLayer2.getStartY(); j < flatChunkGeneratorLayer2.getStartY() + flatChunkGeneratorLayer2.getThickness(); ++j) {
                BlockState blockState = flatChunkGeneratorLayer2.getBlockState();
                if (!blockState.isOf(Blocks.AIR)) {
                    this.hasNoTerrain = false;
                    this.layerBlocks[this.method_31926(j)] = blockState;
                }
            }
        }

    }

    public Biome getBiome() {
        return (Biome)this.biome.get();
    }

    public StructuresConfig getStructuresConfig() {
        return structuresConfig;
    }

    public int method_31926(int i) {
        return i - this.getBottomY();
    }

    @Override
    public int getHeight() {
        return 256;
    }

    @Override
    public int getBottomY() {
        return 0;
    }
}
