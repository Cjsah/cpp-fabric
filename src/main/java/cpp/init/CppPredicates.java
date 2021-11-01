package cpp.init;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.mixin.object.builder.ModelPredicateProviderRegistryAccessor;
import net.fabricmc.fabric.mixin.object.builder.ModelPredicateProviderRegistrySpecificAccessor;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public final class CppPredicates {
    public static void init(){
        FabricModelPredicateProviderRegistry.register(new Identifier("temperancer"), (itemStack, clientWorld, livingEntity, i) -> (float)itemStack.getOrCreateTag().getInt("open"));
    }
}
