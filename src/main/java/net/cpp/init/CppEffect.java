package net.cpp.init;

import net.cpp.api.CppEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;

public final class CppEffect {
    public static final StatusEffect CHAIN = Registry.register(Registry.STATUS_EFFECT, 101,"cpp:chain", new net.cpp.api.CppEffect(StatusEffectType.NEUTRAL, 111));

    public static void register() {}
}
