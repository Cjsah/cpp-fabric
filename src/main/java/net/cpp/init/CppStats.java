package net.cpp.init;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class CppStats {

	public static final Identifier INTERACT_WITH_CRAFTING_MACHINE = registerStats("interact_with_crafting_machine");
	public static final Identifier INTERACT_WITH_ALL_IN_ONE_MACHINE = registerStats("interact_with_all_in_one_machine");
	public static final Identifier INTERACT_WITH_ITEM_PROCESSOR = registerStats("interact_with_item_processor");
	public static final Identifier INTERACT_WITH_MOB_PROJECTOR = registerStats("interact_with_mob_projector");
	public static final Identifier INTERACT_WITH_BEACON_ENHANCER = registerStats("interact_with_beacon_enhancer");

	public static void register() {
	}

	private static Identifier registerStats(String name) {		
		return Registry.register(Registry.CUSTOM_STAT, "cpp:" + name, new Identifier("cpp:" + name));
	}

}
