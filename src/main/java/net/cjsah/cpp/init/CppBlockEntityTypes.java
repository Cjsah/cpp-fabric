package net.cjsah.cpp.init;

import net.cjsah.cpp.blockentity.CraftingMachineBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CppBlockEntityTypes {

    public static final BlockEntityType<CraftingMachineBlockEntity> CRAFTING_MACHINE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("cpp", "crafting_machine"), BlockEntityType.Builder.create(CraftingMachineBlockEntity::new).build(null));;


}
