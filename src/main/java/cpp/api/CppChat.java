package cpp.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import javax.annotation.Nonnull;

public final class CppChat {

    /**
     * 说出以cpp为标题的话
     *
     * @author Cjsah
     * @param user 说话的玩家
     * @param text 说话的内容
     */
    public static void say(@Nonnull PlayerEntity user, Text text) {
        user.sendSystemMessage(
                new TranslatableText("misc.cpp",
                        new TranslatableText("chat.cpp.title").formatted(Formatting.GOLD),
                        text),
                Util.NIL_UUID
        );
    }
}
