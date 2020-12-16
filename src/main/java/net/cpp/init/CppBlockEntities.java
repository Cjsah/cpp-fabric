package net.cpp.init;

import net.cpp.block.entity.AllInOneMachineBlockEntity;
import net.cpp.block.entity.BeaconEnhancerBlockEntity;
import net.cpp.block.entity.CraftingMachineBlockEntity;
import net.cpp.block.entity.ItemProcessorBlockEntity;
import net.cpp.block.entity.MobProjectorBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public final class CppBlockEntities {
	public static final BlockEntityType<CraftingMachineBlockEntity> CRAFTING_MACHINE = registerBlockEntityType(
			CraftingMachineBlockEntity::new, CppBlocks.CRAFTING_MACHINE);
	public static final BlockEntityType<AllInOneMachineBlockEntity> ALL_IN_ONE_MACHINE = registerBlockEntityType(
			AllInOneMachineBlockEntity::new, CppBlocks.ALL_IN_ONE_MACHINE);
	public static final BlockEntityType<ItemProcessorBlockEntity> ITEM_PROCESSER = registerBlockEntityType(
			ItemProcessorBlockEntity::new, CppBlocks.ITEM_PROCESSER);
	public static final BlockEntityType<MobProjectorBlockEntity> MOB_PROJECTOR = registerBlockEntityType(
			MobProjectorBlockEntity::new, CppBlocks.MOB_PROJECTOR);
	public static final BlockEntityType<BeaconEnhancerBlockEntity> BEACON_ENHANCER = registerBlockEntityType(
			BeaconEnhancerBlockEntity::new, CppBlocks.BEACON_ENHANCER);

	public static void register() {
	}

	private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntityType(BlockEntityType.BlockEntityFactory<? extends T> factory, Block block) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, Registry.BLOCK.getId(block).toString(), BlockEntityType.Builder.create(factory, block).build(null));
	}
}
