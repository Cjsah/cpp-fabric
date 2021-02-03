package net.cpp.ducktyping;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface IPlayerJoinCallback {
    Event<IPlayerJoinCallback> EVENT = EventFactory.createArrayBacked(IPlayerJoinCallback.class, (listeners) -> (player, server) -> {
        for (IPlayerJoinCallback listener : listeners) {
            ActionResult result = listener.joinServer(player, server);

            if (result != ActionResult.PASS) {
                return result;
            }
        }
        return ActionResult.PASS;
    });

    ActionResult joinServer(ServerPlayerEntity player, MinecraftServer server);
}
