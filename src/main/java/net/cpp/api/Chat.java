package net.cpp.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

public final class Chat {
    public static void say(PlayerEntity user, TranslatableText translatableText) {
        user.sendSystemMessage(
                new TranslatableText("chat.cpp.announcement",
                        new TranslatableText("chat.cpp.title").formatted(Formatting.GOLD),
                        translatableText),
                Util.NIL_UUID
        );
    }
}
