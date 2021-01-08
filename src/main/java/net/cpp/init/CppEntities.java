package net.cpp.init;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.cpp.entity.AGolemEntity;
import net.cpp.entity.GolemFarmerEntity;
import net.cpp.entity.GolemFisherEntity;
import net.cpp.entity.GolemHerderEntity;
import net.cpp.entity.GolemMinerEntity;
import net.cpp.entity.GolemWarriorEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public final class CppEntities {
	public static final EntityType<GolemFarmerEntity> GOLEM_FARMER = register("golem_famrer", GolemFarmerEntity::new, SpawnGroup.MISC);
	public static final EntityType<GolemMinerEntity> GOLEM_MINER = register("golem_miner", GolemMinerEntity::new, SpawnGroup.MISC);
	public static final EntityType<GolemFisherEntity> GOLEM_FISHER = register("golem_fisher", GolemFisherEntity::new, SpawnGroup.MISC);
	public static final EntityType<GolemWarriorEntity> GOLEM_WARRIOR = register("golem_warrior", GolemWarriorEntity::new, SpawnGroup.MISC);
	public static final EntityType<GolemHerderEntity> GOLEM_HERDER = register("golem_herder", GolemHerderEntity::new, SpawnGroup.MISC);
	public static final List<EntityType<? extends AGolemEntity>> GOLEMS = ImmutableList.of(GOLEM_FARMER,GOLEM_MINER,GOLEM_FISHER,GOLEM_WARRIOR,GOLEM_HERDER);

	private CppEntities() {
	}

	public static void register() {
	}

	private static <T extends LivingEntity> EntityType<T> register(String id, EntityType.EntityFactory<T> factory, SpawnGroup group) {
		EntityType<T> type = Registry.register(Registry.ENTITY_TYPE, "cpp:" + id, EntityType.Builder.create(factory, group).build(id));
		FabricDefaultAttributeRegistry.register(type, LivingEntity.createLivingAttributes());
		return type;
	}

	static {
	}
}
