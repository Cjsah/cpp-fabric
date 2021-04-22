package cpp.mixin;

import cpp.Craftingpp;
import cpp.init.CppBlocks;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(SkullBlockEntityRenderer.class)

public abstract class MixinSkullBlockEntityRenderer {
	
	@Shadow @Final private static Map<SkullBlock.SkullType, Identifier> TEXTURES;
	
	static {
		TEXTURES.put(CppBlocks.BAT_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/bat.png"));
		TEXTURES.put(CppBlocks.BEE_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/bee/bee.png"));
		TEXTURES.put(CppBlocks.BLAZE_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/blaze.png"));
		TEXTURES.put(CppBlocks.BROWN_MOOSHROOM_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/cow/brown_mooshroom.png"));
		TEXTURES.put(CppBlocks.CAVE_SPIDER_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/spider/cave_spider.png"));
		TEXTURES.put(CppBlocks.CHICKEN_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/chicken.png"));
		TEXTURES.put(CppBlocks.COD_HEAD.getFirst().getSkullType(), new Identifier(Craftingpp.MOD_ID3, "textures/head/cod.png"));
		TEXTURES.put(CppBlocks.COW_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/cow/cow.png"));
		TEXTURES.put(CppBlocks.DOLPHIN_HEAD.getFirst().getSkullType(), new Identifier(Craftingpp.MOD_ID3, "textures/head/dolphin.png"));
		TEXTURES.put(CppBlocks.DONKEY_HEAD.getFirst().getSkullType(), new Identifier(Craftingpp.MOD_ID3, "textures/head/donkey.png"));
		TEXTURES.put(CppBlocks.DROWNED_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/zombie/drowned.png"));
		TEXTURES.put(CppBlocks.ELDER_GUARDIAN_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/guardian_elder.png"));
		TEXTURES.put(CppBlocks.ENDERMAN_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/enderman/enderman.png"));
		TEXTURES.put(CppBlocks.ENDERMITE_HEAD.getFirst().getSkullType(), new Identifier(Craftingpp.MOD_ID3, "textures/head/endermite.png"));
		TEXTURES.put(CppBlocks.EVOKER_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/illager/evoker.png"));
		TEXTURES.put(CppBlocks.FOX_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/fox/fox.png"));
		TEXTURES.put(CppBlocks.GHAST_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/ghast/ghast.png"));
		TEXTURES.put(CppBlocks.GLOW_SQUID_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/squid/glow_squid.png"));
		TEXTURES.put(CppBlocks.GOAT_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/goat/goat.png"));
		TEXTURES.put(CppBlocks.GUARDIAN_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/guardian.png"));
		TEXTURES.put(CppBlocks.HORSE_HEAD.getFirst().getSkullType(), new Identifier(Craftingpp.MOD_ID3, "textures/head/horse.png"));
		TEXTURES.put(CppBlocks.HUSK_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/zombie/husk.png"));
		TEXTURES.put(CppBlocks.ILLUSIONER_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/illager/illusioner.png"));
		TEXTURES.put(CppBlocks.IRON_GOLEM_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/iron_golem/iron_golem.png"));
		TEXTURES.put(CppBlocks.LLAMA_HEAD.getFirst().getSkullType(), new Identifier(Craftingpp.MOD_ID3, "textures/head/llama.png"));
		TEXTURES.put(CppBlocks.MAGMA_CUBE_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/slime/magmacube.png"));
		TEXTURES.put(CppBlocks.MULE_HEAD.getFirst().getSkullType(), new Identifier(Craftingpp.MOD_ID3, "textures/head/mule.png"));
		TEXTURES.put(CppBlocks.PANDA_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/panda/panda.png"));
		TEXTURES.put(CppBlocks.PHANTOM_HEAD.getFirst().getSkullType(), new Identifier(Craftingpp.MOD_ID3, "textures/head/phantom.png"));
		TEXTURES.put(CppBlocks.PIG_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/pig/pig.png"));
		TEXTURES.put(CppBlocks.PIGLIN_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/piglin/piglin.png"));
		TEXTURES.put(CppBlocks.PILLAGER_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/illager/pillager.png"));
		TEXTURES.put(CppBlocks.POLAR_BEAR_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/bear/polarbear.png"));
		TEXTURES.put(CppBlocks.PUFFERFISH_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/fish/pufferfish.png"));
		TEXTURES.put(CppBlocks.RAVAGER_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/illager/ravager.png"));
		TEXTURES.put(CppBlocks.RED_MOOSHROOM_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/cow/red_mooshroom.png"));
		TEXTURES.put(CppBlocks.SALMON_HEAD.getFirst().getSkullType(), new Identifier(Craftingpp.MOD_ID3, "textures/head/salmon.png"));
		TEXTURES.put(CppBlocks.SHEEP_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/sheep/sheep.png"));
		TEXTURES.put(CppBlocks.SHULKER_HEAD.getFirst().getSkullType(), new Identifier(Craftingpp.MOD_ID3, "textures/head/shulker.png"));
		TEXTURES.put(CppBlocks.SKELETON_HORSE_HEAD.getFirst().getSkullType(), new Identifier(Craftingpp.MOD_ID3, "textures/head/skeleton_horse.png"));
		TEXTURES.put(CppBlocks.SLIME_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/slime/slime.png"));
		TEXTURES.put(CppBlocks.SNOW_FOX_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/fox/snow_fox.png"));
		TEXTURES.put(CppBlocks.SNOW_GOLEM_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/snow_golem.png"));
		TEXTURES.put(CppBlocks.SPIDER_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/spider/spider.png"));
		TEXTURES.put(CppBlocks.SQUID_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/squid/squid.png"));
		TEXTURES.put(CppBlocks.STRAY_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/skeleton/stray.png"));
		TEXTURES.put(CppBlocks.TRADER_LLAMA_HEAD.getFirst().getSkullType(), new Identifier(Craftingpp.MOD_ID3, "textures/head/trader_llama.png"));
		TEXTURES.put(CppBlocks.TROPICAL_FISH_HEAD.getFirst().getSkullType(), new Identifier(Craftingpp.MOD_ID3, "textures/head/tropical_fish.png"));
		TEXTURES.put(CppBlocks.TURTLE_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/turtle/big_sea_turtle.png"));
		TEXTURES.put(CppBlocks.VEX_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/illager/vex.png"));
		TEXTURES.put(CppBlocks.VILLAGER_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/villager/villager.png"));
		TEXTURES.put(CppBlocks.VINDICATOR_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/illager/vindicator.png"));
		TEXTURES.put(CppBlocks.WANDERING_TRADER_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/wandering_trader.png"));
		TEXTURES.put(CppBlocks.WITCH_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/witch.png"));
		TEXTURES.put(CppBlocks.WOLF_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/wolf/wolf.png"));
		TEXTURES.put(CppBlocks.ZOMBIE_HORSE_HEAD.getFirst().getSkullType(), new Identifier(Craftingpp.MOD_ID3, "textures/head/zombie_horse.png"));
		TEXTURES.put(CppBlocks.ZOMBIE_VILLAGER_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/zombie_villager/zombie_villager.png"));
		TEXTURES.put(CppBlocks.ZOMBIFIED_PIGLIN_HEAD.getFirst().getSkullType(), new Identifier("textures/entity/piglin/zombified_piglin.png"));
	}
}
