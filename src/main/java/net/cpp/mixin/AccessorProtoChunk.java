package net.cpp.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ProtoChunk.class)
public interface AccessorProtoChunk {
    @Accessor("lightSources")
    List<BlockPos> getLightSources();

}
