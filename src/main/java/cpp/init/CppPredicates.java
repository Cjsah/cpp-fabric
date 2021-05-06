package cpp.init;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public final class CppPredicates {
    public static void init(){
        FabricModelPredicateProviderRegistry.register(new Identifier("character"), (itemStack, clientWorld, livingEntity, i) -> (float)itemStack.getOrCreateTag().getInt("character"));
        FabricModelPredicateProviderRegistry.register(new Identifier("temperancer"), (itemStack, clientWorld, livingEntity, i) -> (float)itemStack.getOrCreateTag().getInt("open"));
    }
}
