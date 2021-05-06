package cpp.init;

import com.mojang.brigadier.Command;
import cpp.rituals.RitualResult;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;

public final class CppCommands {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> dispatcher.register(
                CommandManager.literal("cpp").then(CommandManager.literal("test").executes(commandContext -> {
                    commandContext.getSource().getPlayer().giveItemStack(RitualResult.getEffectResult(CppItems.AGENTIA_OF_CHAIN, new ItemStack(Items.DIAMOND_BOOTS)));
                    return Command.SINGLE_SUCCESS;
                }))
        ));


    }
}
