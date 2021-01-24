package net.cpp.init;

import net.cpp.structure.EnchantingRoomFeature;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public final class CppFeatures {
	public static final EnchantingRoomFeature ENCHANTING_ROOM_FEATURE;
	public static final ConfiguredStructureFeature<?, ?> ENCHANTING_ROOM_CONFIGURED;
	public static void loadClass() {
		
	}
	static {
		{
			EnchantingRoomFeature feature = new EnchantingRoomFeature(DefaultFeatureConfig.CODEC);
			ConfiguredStructureFeature<DefaultFeatureConfig, ? extends StructureFeature<DefaultFeatureConfig>> configuredStructure = feature.configure(new DefaultFeatureConfig());
			ENCHANTING_ROOM_FEATURE = FabricStructureBuilder.create(new Identifier("cpp:enchanting_room"), feature).step(Feature.SURFACE_STRUCTURES).defaultConfig(32, 8, 12345).superflatFeature(configuredStructure).register();
			ENCHANTING_ROOM_CONFIGURED = ENCHANTING_ROOM_FEATURE.configure(DefaultFeatureConfig.DEFAULT);
			RegistryKey<ConfiguredStructureFeature<?, ?>> myConfigured = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN, new Identifier("cpp", "enchanting_room"));
			BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, myConfigured.getValue(), ENCHANTING_ROOM_CONFIGURED);
			BiomeModifications.addStructure(BiomeSelectors.all(), myConfigured);
		}
	}
}
