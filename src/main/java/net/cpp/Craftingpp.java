package net.cpp;

import java.io.IOException;

import net.cpp.init.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class Craftingpp implements ModInitializer {

	public static final ItemGroup CPP_GROUP = FabricItemGroupBuilder.create(new Identifier("cpp", "title"))
			.icon(() -> new ItemStack(CppBlocks.CRAFTING_MACHINE)).build();

	@Override
	public void onInitialize() {
		CppStats.register();
		CppItems.register();
		CppBlocks.register();
		CppBlockEntities.register();
		CppScreenHandler.register();
		CppRecipes.register();
	}
}
