package net.cjsah.cpp.init;

import net.cjsah.cpp.block.CraftingMachineBlock;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class CraftingppItems {

    public static final Item CRAFTING_MACHINE = new CraftingMachineBlock(new Item.Settings().rarity(Rarity.COMMON));




    public static final ItemGroup CPP_GROUP = FabricItemGroupBuilder.create(
            new Identifier("cpp", "title"))
            .icon(() -> new ItemStack(CRAFTING_MACHINE)).appendItems(stacks -> {
                stacks.add(new ItemStack(CRAFTING_MACHINE));
            }).build();

}
