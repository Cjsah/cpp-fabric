package net.cpp.init;

import net.cpp.Craftingpp;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

@SuppressWarnings("unused")
public final class CppDimensionTypes {
	public static final Identifier FLOWER_ID = new Identifier(Craftingpp.MOD_ID3, "flower");
	public static final RegistryKey<DimensionType> FLOWER_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, FLOWER_ID);
	private static DimensionType Flower;// = DimensionType.method_32922(OptionalLong.of(6000), true, false, false, true, 1., false, false, true, true, false, 0, 256, 256, HorizontalVoronoiBiomeAccessType.INSTANCE, CppBlockTags.EMPTY.getId(), FLOWER_ID, 1f);

	public static DimensionType getFlower() {
		return Flower;
	}

	public static void register(MinecraftServer server) {
		if (Flower == null) {
			Flower = server.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY).get(FLOWER_ID);
		}
	}

	static {
		ServerLifecycleEvents.SERVER_STARTED.register(CppDimensionTypes::register);
	}
}
