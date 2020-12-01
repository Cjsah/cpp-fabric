package net.cpp.init;

import net.cpp.block.entity.AllInOneMachineBlockEntity;
import net.cpp.block.entity.CraftingMachineBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public final class CppBlockEntities {
	public static final BlockEntityType<CraftingMachineBlockEntity> CRAFTING_MACHINE = Registry.register(
			Registry.BLOCK_ENTITY_TYPE, "cpp:crafting_machine",
			BlockEntityType.Builder.create(CraftingMachineBlockEntity::new, CppBlocks.CRAFTING_MACHINE).build(null));
	public static final BlockEntityType<AllInOneMachineBlockEntity> ALL_IN_ONE_MACHINE = Registry.register(
			Registry.BLOCK_ENTITY_TYPE, "cpp:all_in_one_machine",
			BlockEntityType.Builder.create(AllInOneMachineBlockEntity::new, CppBlocks.ALL_IN_ONE_MACHINE).build(null));
	public static void register() {
	}

}
