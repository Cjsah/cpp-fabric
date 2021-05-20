package cpp.item;

import cpp.api.Utils;
import cpp.init.CppItems;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.SetTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Grafter extends MiningToolItem {
	/**
	 * 键：树叶<br>
	 * 值：对应的树苗
	 */
	public static final Map<Block, Block> LEAVES_TO_SALPING = new HashMap<>();

	public Grafter(Settings settings) {
		this(-2, -1, ToolMaterials.IRON, settings);
	}

	protected Grafter(float attackDamage, float attackSpeed, ToolMaterial material, Settings settings) {
		super(attackDamage, attackSpeed, material, SetTag.of(LEAVES_TO_SALPING.keySet()) , settings);
	}

	public boolean isSuitableFor(BlockState state) {
		return LEAVES_TO_SALPING.containsKey(state.getBlock());
	}

	static {
		/**
		 * 添加有对应树苗的树叶
		 */
		for (Entry<RegistryKey<Block>, Block> entry : Registry.BLOCK.getEntries()) {
			if (entry.getKey().getValue().getPath().contains("leaves")) {
				Identifier id = new Identifier(entry.getKey().getValue().getNamespace(), entry.getKey().getValue().getPath().replaceFirst("leaves", "sapling"));
				if (Registry.BLOCK.containsId(id)) {
					LEAVES_TO_SALPING.put(entry.getValue(), Registry.BLOCK.get(id));
				}
			}
		}

		/**
		 * 如果用剪枝器挖掘树叶，则阻止正常掉落物，改为掉落树苗
		 */
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> {
			if (player.getMainHandStack().isOf(CppItems.GRAFTER) && LEAVES_TO_SALPING.containsKey(state.getBlock())) {
				Utils.excavate((ServerWorld) world, player, pos, null);
				if (!player.isCreative())
					Block.dropStack(world, pos, LEAVES_TO_SALPING.get(state.getBlock()).asItem().getDefaultStack());
				return false;
			}
			return true;
		});
	}
}
