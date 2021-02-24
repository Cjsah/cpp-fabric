package net.cpp.init;

import net.cpp.Craftingpp;
import net.cpp.api.ChainProcessor;
import net.cpp.api.CppEffect;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class CppEffects {
	
	public static final StatusEffect CHAIN = register("chain", new CppEffect(StatusEffectType.NEUTRAL, 11250603));

	public static void register() {
		// 连环药水效果
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> {
			if (player.getStatusEffect(CppEffects.CHAIN) != null && ChainProcessor.canChain(player.getMainHandStack().getItem(), state) && player.getMainHandStack().getDamage() + 1 < player.getMainHandStack().getMaxDamage()) {
				new ChainProcessor((ServerWorld) world, pos, world.getBlockState(pos).getBlock(), player.getMainHandStack(), (ServerPlayerEntity) player).start();
				return false;
			}
			return true;
		});
	}
	
	private static <T extends StatusEffect> T register(String name, T effectClass) {
		return Registry.register(Registry.STATUS_EFFECT, new Identifier(Craftingpp.MOD_ID3, name), effectClass);
	}
	
}
