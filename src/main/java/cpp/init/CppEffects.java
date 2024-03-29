package cpp.init;

import cpp.Craftingpp;
import cpp.publicmc.PublicStatusEffect;
import cpp.misc.ChainProcessor;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class CppEffects {
	
	public static final StatusEffect CHAIN = register("chain", new PublicStatusEffect(StatusEffectType.NEUTRAL, 11250603));

	public static void init() {
		// 连环药水效果
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> {
			if (player.getStatusEffect(CppEffects.CHAIN) != null && ChainProcessor.canChain(player.getMainHandStack().getItem(), state) && player.getMainHandStack().getDamage() + 1 < player.getMainHandStack().getMaxDamage()) {
				new ChainProcessor((ServerWorld) world, pos, world.getBlockState(pos).getBlock(), player.getMainHandStack(), (ServerPlayerEntity) player).start();
				return false;
			}
			return true;
		});
	}
	
	@SuppressWarnings("SameParameterValue")
	private static <T extends StatusEffect> T register(String name, T effectClass) {
		return Registry.register(Registry.STATUS_EFFECT, new Identifier(Craftingpp.MOD_ID3, name), effectClass);
	}
	
}
