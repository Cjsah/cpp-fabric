package net.cpp.init;

import net.cpp.api.CppChain;
import net.cpp.api.CppEffect;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.Registry;

public final class CppEffects {
    public static final StatusEffect CHAIN = Registry.register(Registry.STATUS_EFFECT, 101,"cpp:chain", new CppEffect(StatusEffectType.NEUTRAL, 11250603));

    public static void register() {
        // 连环药水效果
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            StatusEffectInstance effect = player.getStatusEffect(CppEffects.CHAIN);
            if (effect != null && CppChainMap.ChainBlocks.contains(state.getBlock()) && CppChainMap.ChainTools.contains(player.getMainHandStack().getItem())) {
                CppChain.chain(world, (ServerPlayerEntity) player, pos, state.getBlock());
            }
        });
    }
}
