package net.cpp.init;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class CppStats {

    public static final Identifier INTERACT_WITH_CRAFTING_MACHINE = registerStats("interact_with_crafting_machine");









    public static void register() {}

    private static Identifier registerStats(String name) {
        Identifier identifier = new Identifier("cpp:"+name);
        Registry.register(Registry.CUSTOM_STAT, "cpp:" + name, identifier);
        return identifier;
    }

}
