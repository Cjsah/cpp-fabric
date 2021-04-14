package cpp.init;

import com.mojang.brigadier.Command;
import cpp.rituals.RitualResult;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;

public final class CppCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> dispatcher.register(
                CommandManager.literal("cpp").then(CommandManager.literal("test").executes(commandContext -> {
                    RitualResult.getEffectResult(CppItems.AGENTIA_OF_AGILENESS, CppItems.SPIRIT_OF_LIFE.getDefaultStack());
//                    commandContext.getSource().getPlayer().giveItemStack(RitualResult.getEnchantingResult(CppItems.WING_OF_SKY, CppItems.SPIRIT_OF_LIFE));
                    return Command.SINGLE_SUCCESS;
                }))
        ));


    }
}
