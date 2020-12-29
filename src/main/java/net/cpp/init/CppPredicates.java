package net.cpp.init;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class CppPredicates {
    public static void register(){
        FabricModelPredicateProviderRegistry.register(new Identifier("character"), (itemStack, clientWorld, livingEntity, i) -> itemStack.hasTag() ? (float)itemStack.getOrCreateTag().getInt("character") : 0.0F);
    }
}
