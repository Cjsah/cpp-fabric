package net.cpp.init;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class CppChannels {
	public static final Identifier COLOR_PALETTE = new Identifier("cpp", "color_palette");

	static Identifier register(String name, PlayChannelHandler playChannelHandler) {
		Identifier identifier = new Identifier("cpp", name);
		ClientPlayNetworking.registerReceiver(identifier, playChannelHandler);
		return identifier;
	}

	static {
		ServerPlayNetworking.registerReceiver(null, COLOR_PALETTE, (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
		});
	}
}
