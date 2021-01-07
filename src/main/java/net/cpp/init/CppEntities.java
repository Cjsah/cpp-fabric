package net.cpp.init;

import net.cpp.entity.GolemMiner;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public final class CppEntities {
	public static final EntityType<GolemMiner> GOLEM_MINER = register("moving_golem", GolemMiner::new, SpawnGroup.MISC);

	private CppEntities() {
	}

	public static void register() {
	}

	private static <T extends Entity> EntityType<T> register(String id, EntityType.EntityFactory<T> factory, SpawnGroup group) {
		return Registry.register(Registry.ENTITY_TYPE, "cpp:" + id, EntityType.Builder.create(factory, group).build(id));
	}

	static {
		FabricDefaultAttributeRegistry.register(CppEntities.GOLEM_MINER, LivingEntity.createLivingAttributes());
	}
}
