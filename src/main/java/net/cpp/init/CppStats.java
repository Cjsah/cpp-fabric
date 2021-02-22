package net.cpp.init;

import net.cpp.Craftingpp;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class CppStats {

	public static final Identifier INTERACT_WITH_CRAFTING_MACHINE = registerStats("interact_with_crafting_machine");
	public static final Identifier INTERACT_WITH_ALL_IN_ONE_MACHINE = registerStats("interact_with_all_in_one_machine");
	public static final Identifier INTERACT_WITH_ITEM_PROCESSOR = registerStats("interact_with_item_processor");
	public static final Identifier INTERACT_WITH_MOB_PROJECTOR = registerStats("interact_with_mob_projector");
	public static final Identifier INTERACT_WITH_BEACON_ENHANCER = registerStats("interact_with_beacon_enhancer");
	public static final Identifier INTERACT_WITH_TRADE_MACHINE = registerStats("interact_with_trade_machine");
	public static final Identifier INTERACT_WITH_GOLD_ANVIL = registerStats("interact_with_gold_anvil");
	public static final Identifier INTERACT_WITH_DUSTBIN = registerStats("interact_with_dustbin");
	public static final Identifier INTERACT_WITH_CHEST_DROPPER = registerStats("interact_with_chest_dropper");
	public static final Identifier INTERACT_WITH_EMPTY_BOOKSHELF = registerStats("interact_with_empty_bookshelf");
	public static final Identifier INTERACT_WITH_BLOCK_BREAKER = registerStats("interact_with_block_breaker");

	public static void loadClass() {}

	private static Identifier registerStats(String name) {		
		return Registry.register(Registry.CUSTOM_STAT, new Identifier(Craftingpp.MOD_ID3, name), new Identifier("cpp:" + name));
	}

}
