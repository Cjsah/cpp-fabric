package cpp.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import javax.annotation.Nonnull;

public final class CppChat {

    /**
     * 对{@link PlayerEntity}发送以cpp为标题的内容
     *
     * @author Cjsah
     * @param player 玩家
     * @param text 内容
     */
    public static void say(@Nonnull PlayerEntity player, Text text) {
        player.sendSystemMessage(
                new TranslatableText("misc.cpp",
                        new TranslatableText("chat.cpp.title").formatted(Formatting.GOLD),
                        text),
                Util.NIL_UUID
        );
    }
}
