package net.cpp.init;


import net.cpp.Craftingpp;
import net.cpp.misc.AncientScrollRandomTag;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;

public class CppLootTableFunctions {

    public static final LootFunctionType ANCIENT_SCROLL_RANDOM_TAG = register("ancient_scroll_random_tag", new AncientScrollRandomTag.Serializer());


    private static LootFunctionType register(String id, JsonSerializer<? extends LootFunction> jsonSerializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, new Identifier(Craftingpp.MOD_ID3, id), new LootFunctionType(jsonSerializer));
    }

    public static void register() {}
}
