package net.cpp.init;

import net.cpp.api.CppEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;

public final class CppEffects {
    public static final StatusEffect CHAIN = Registry.register(Registry.STATUS_EFFECT, 101,"cpp:chain", new CppEffect(StatusEffectType.NEUTRAL, 11250603));

    public static void register() {}
}
