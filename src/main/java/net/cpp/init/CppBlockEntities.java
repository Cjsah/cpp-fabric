package net.cpp.init;

import net.cpp.block.entity.CraftingMachineBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class CppBlockEntities {
    public static BlockEntityType<CraftingMachineBlockEntity> CRAFTING_MACHINE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("cpp", "crafting_machine"), BlockEntityType.Builder.create(CraftingMachineBlockEntity::new).build(null));



    public static void register() {
    }

}
