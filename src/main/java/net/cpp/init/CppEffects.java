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
	public static final StatusEffect DARKNESS = new CppEffect(StatusEffectType.HARMFUL, 0);
	
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
	
	public static class VaccineStatusEffect extends StatusEffect {
		public static final VaccineStatusEffect BLINDNESS = register("vaccine/blindness", new VaccineStatusEffect(StatusEffectType.BENEFICIAL, 2039587, StatusEffects.BLINDNESS));
		public static final VaccineStatusEffect DARKNESS = register("vaccine/darkness", new VaccineStatusEffect(StatusEffectType.BENEFICIAL, 1908001, CppEffects.DARKNESS));
		public static final VaccineStatusEffect MINING_FATIGUE = register("vaccine/mining_fatigue", new VaccineStatusEffect(StatusEffectType.BENEFICIAL, 4866583, StatusEffects.MINING_FATIGUE));
		public static final VaccineStatusEffect POISON = register("vaccine/poison", new VaccineStatusEffect(StatusEffectType.BENEFICIAL, 5149489, StatusEffects.POISON));
		public static final VaccineStatusEffect WITHER = register("vaccine/wither", new VaccineStatusEffect(StatusEffectType.BENEFICIAL, 3484199, StatusEffects.WITHER));
		private final StatusEffect immuneEffect;
		
		public VaccineStatusEffect(StatusEffectType type, int color, StatusEffect immuneEffect) {
			super(type, color);
			this.immuneEffect = immuneEffect;
		}
		
		public StatusEffect getImmuneEffect() {
			return immuneEffect;
		}
		public static void loadClass() {
		
		}
	}
}
