package cpp.mixin;

import cpp.Craftingpp;
import cpp.ducktyping.ICppState;
import cpp.state.CppStateOperate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@SuppressWarnings("unused")
@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer implements ICppState {

    @Shadow
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Map<RegistryKey<World>, ServerWorld> worlds;

    private final CppStateOperate operate = new CppStateOperate();

    @Override
    public CppStateOperate getCppStateOperate() {
        return this.operate;
    }

    private void initCppState(PersistentStateManager persistentStateManager) {
        persistentStateManager.getOrCreate(this.getCppStateOperate()::stateFromNbt, this.getCppStateOperate()::initState, Craftingpp.MOD_ID3);
    }

    @Inject(at = @At("RETURN"), method = "createWorlds")
    public void createWorlds(CallbackInfo info) {
        this.initCppState(this.worlds.get(World.OVERWORLD).getPersistentStateManager());
    }

}
