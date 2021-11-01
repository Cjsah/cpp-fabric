package cpp.mixin;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(ModelPredicateProviderRegistry.class)
public class MixinModelPredicateProviderRegistry {

    @Final
    @Shadow
    @SuppressWarnings("deprecation")
    private static final Map<Identifier, ModelPredicateProvider> GLOBAL = Maps.newHashMap();


    @Inject(at = @At("RETURN"),method = "<clinit>")
    private static void register(CallbackInfo ci) {
        GLOBAL.put(new Identifier("character"), (stack, world, entity, seed) -> stack.getOrCreateTag().getInt("character"));
    }
}
