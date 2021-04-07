package cpp.init;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionOptions;

@SuppressWarnings("unused")
public final class CppDimensionOptions {
	public static final RegistryKey<DimensionOptions> FLOWER = RegistryKey.of(Registry.DIMENSION_OPTIONS,CppDimensionTypes.FLOWER_ID);
}
