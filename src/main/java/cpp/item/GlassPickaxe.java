package cpp.item;

import cpp.api.Utils;
import cpp.init.CppItems;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class GlassPickaxe extends PickaxeItem {
	public static final Set<Block> GLASSES = new HashSet<>();

	public GlassPickaxe(Settings settings) {
		super(ToolMaterials.STONE, 1, -2.8f, settings);
	}

	public boolean isSuitableFor(BlockState state) {
		return GLASSES.contains(state.getBlock());
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return isSuitableFor(state) ? miningSpeed * 2 : super.getMiningSpeedMultiplier(stack, state);
	}

	static {
		/**
		 * 添加玻璃
		 */
		for (Entry<RegistryKey<Block>, Block> entry : Registry.BLOCK.getEntries()) {
			if (entry.getKey().getValue().getPath().contains("glass")) {
				GLASSES.add(entry.getValue());
			}
		}

		/**
		 * 如果用玻璃镐挖掘玻璃，则阻止正常掉落物，改为掉落玻璃
		 */
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> {
			if (player.getMainHandStack().isOf(CppItems.GLASS_PICKAXE) && GLASSES.contains(state.getBlock())) {
				Utils.excavate((ServerWorld) world, (ServerPlayerEntity) player, pos, null);
				if (!player.isCreative())
					Block.dropStack(world, pos, state.getBlock().asItem().getDefaultStack());
				return false;
			}
			return true;
		});
	}
}
