package cpp.mixin;

import com.mojang.datafixers.util.Either;
import cpp.island.IslandChunkGenerator;
import cpp.island.IslandUtils;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

@Mixin(ChunkStatus.class)
@SuppressWarnings("unused")
public class MixinChunkStatus {
//    @Inject(method = "doWork(Lnet/minecraft/world/chunk/ChunkStatus;Ljava/util/concurrent/Executor;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/structure/StructureManager;Lnet/minecraft/server/world/ServerLightingProvider;Ljava/util/function/Function;Ljava/util/List;Lnet/minecraft/world/chunk/Chunk;)V", at = @At("HEAD"))
    @Inject(method = "method_20613", at = @At("HEAD"))
    private static void onLighting(ChunkStatus chunkStatus, Executor executor, ServerWorld world, ChunkGenerator generator, StructureManager manager, ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> function, List<Chunk> list, Chunk chunk, CallbackInfoReturnable<CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> info) {
        if(generator instanceof IslandChunkGenerator && !chunk.getStatus().isAtLeast(chunkStatus)) IslandUtils.generator((ProtoChunk) chunk, world);
    }

    @Inject(method = "method_16566", at = @At("RETURN"))
//    @Inject(method = "doWork(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;IILjava/util/List;Lnet/minecraft/world/chunk/Chunk;)V", at = @At("RETURN"))
    private static void afterPopulation(ServerWorld world, ChunkGenerator generator, List<Chunk> list, Chunk chunk, CallbackInfo info) {
        if (generator instanceof IslandChunkGenerator) ((ProtoChunk) chunk).getEntities().clear();
    }
}