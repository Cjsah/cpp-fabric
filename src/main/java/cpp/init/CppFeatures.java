package cpp.init;

import cpp.feature.EnchantingRoomFeature;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
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
	public static void loadClass() {}
	static {
		{
			EnchantingRoomFeature feature = new EnchantingRoomFeature(DefaultFeatureConfig.CODEC);
			ConfiguredStructureFeature<DefaultFeatureConfig, ? extends StructureFeature<DefaultFeatureConfig>> configuredStructure = feature.configure(new DefaultFeatureConfig());
			ENCHANTING_ROOM_FEATURE = FabricStructureBuilder.create(EnchantingRoomFeature.Generator.STRUCTURE_ID, feature).step(Feature.SURFACE_STRUCTURES).defaultConfig(1, 1, 12345).superflatFeature(configuredStructure).register();
			ENCHANTING_ROOM_CONFIGURED = ENCHANTING_ROOM_FEATURE.configure(DefaultFeatureConfig.DEFAULT);
			RegistryKey<ConfiguredStructureFeature<?, ?>> myConfigured = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN, EnchantingRoomFeature.Generator.STRUCTURE_ID);
			BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, myConfigured.getValue(), ENCHANTING_ROOM_CONFIGURED);
			
		}
	}
}
