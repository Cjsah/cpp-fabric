package net.cpp.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

public final class Chat {

    /**
     * 说出以cpp为标题的话
     *
     * @author Cjsah
     * @param user 说话的玩家
     * @param translatableText 说话的内容
     */
    public static void say(PlayerEntity user, TranslatableText translatableText) {
        user.sendSystemMessage(
                new TranslatableText("chat.cpp.announcement",
                        new TranslatableText("chat.cpp.title").formatted(Formatting.GOLD),
                        translatableText),
                Util.NIL_UUID
        );
    }
}
