package cpp.init;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public final class CppWorlds {

	public static final RegistryKey<World> FLOWER_KEY = RegistryKey.of(Registry.WORLD_KEY, CppDimensionTypes.FLOWER_ID);
	

}
