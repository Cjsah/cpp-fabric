package net.cjsah.cpp.init;

import net.cjsah.cpp.blockentity.CraftingMachineBlockEntity;
import net.cjsah.cpp.gui.handler.CraftingMachineScreenHandler;
import net.fabricmc.fabric.impl.container.ContainerProviderImpl;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CppBlockEntities {
    public static BlockEntityType<CraftingMachineBlockEntity> CRAFTING_MACHINE;


    public static final Identifier CRAFTING_MACHINE_CONTAINER = new Identifier("cpp", "crafting_machine");


    public static void register() {
        CRAFTING_MACHINE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("cpp", "crafting_machine"), BlockEntityType.Builder.create(CraftingMachineBlockEntity::new).build(null));
    }


    public static void registerServerGUI() {
        ContainerProviderImpl.INSTANCE.registerFactory(CRAFTING_MACHINE_CONTAINER, (synaId, identifier, player, buf) -> new CraftingMachineScreenHandler(synaId, player.inventory, (CraftingMachineBlockEntity)player.world.getBlockEntity(buf.readBlockPos())));
    }
}
