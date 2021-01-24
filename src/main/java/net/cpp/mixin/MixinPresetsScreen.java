package net.cpp.mixin;

import net.cpp.init.CppBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.PresetsScreen;
import net.minecraft.item.ItemConvertible;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.List;

@Mixin(PresetsScreen.class)
public abstract class MixinPresetsScreen {

    @Shadow
    private static void addPreset(Text text, ItemConvertible icon, RegistryKey<Biome> registryKey, List<StructureFeature<?>> structures, boolean bl, boolean bl2, boolean bl3, FlatChunkGeneratorLayer... flatChunkGeneratorLayers) {}


    static {
        addPreset(new TranslatableText("newWorld.cpp.title"), CppBlocks.CRAFTING_MACHINE, BiomeKeys.FLOWER_FOREST, Collections.emptyList(), false, true, false, new FlatChunkGeneratorLayer(1, Blocks.AIR));
    }
}
