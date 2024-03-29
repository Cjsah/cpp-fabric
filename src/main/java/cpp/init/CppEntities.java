package cpp.init;

import com.google.common.collect.ImmutableList;
import cpp.Craftingpp;
import cpp.entity.AGolemEntity;
import cpp.entity.CharacterEntity;
import cpp.entity.DarkChickenEntity;
import cpp.entity.DarkCowEntity;
import cpp.entity.DarkMooshroomEntity;
import cpp.entity.DarkPigEntity;
import cpp.entity.DarkSheepEntity;
import cpp.entity.GolemFarmerEntity;
import cpp.entity.GolemFisherEntity;
import cpp.entity.GolemHerderEntity;
import cpp.entity.GolemMinerEntity;
import cpp.entity.GolemWarriorEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.function.UnaryOperator;

public final class CppEntities {
	public static final EntityType<GolemFarmerEntity> GOLEM_FARMER = registerGolem("golem_famrer", GolemFarmerEntity::new);
	public static final EntityType<GolemMinerEntity> GOLEM_MINER = registerGolem("golem_miner", GolemMinerEntity::new);
	public static final EntityType<GolemFisherEntity> GOLEM_FISHER = registerGolem("golem_fisher", GolemFisherEntity::new);
	public static final EntityType<GolemWarriorEntity> GOLEM_WARRIOR = registerGolem("golem_warrior", GolemWarriorEntity::new);
	public static final EntityType<GolemHerderEntity> GOLEM_HERDER = registerGolem("golem_herder", GolemHerderEntity::new);
	public static final List<EntityType<? extends AGolemEntity>> GOLEMS = ImmutableList.of(GOLEM_FARMER, GOLEM_MINER, GOLEM_FISHER, GOLEM_WARRIOR, GOLEM_HERDER);
	public static final EntityType<DarkCowEntity> DARK_COW = registerDark("dark_cow", DarkCowEntity::new, CowEntity.createCowAttributes(), builder -> builder.setDimensions(0.9F, 1.4F));
	public static final EntityType<DarkMooshroomEntity> DARK_MOOSHROOM = registerDark("dark_mooshroom", DarkMooshroomEntity::new, CowEntity.createCowAttributes(), builder -> builder.setDimensions(0.9F, 1.4F));
	public static final EntityType<DarkSheepEntity> DARK_SHEEP = registerDark("dark_sheep", DarkSheepEntity::new, SheepEntity.createSheepAttributes(), builder -> builder.setDimensions(0.9F, 1.3F));
	public static final EntityType<DarkPigEntity> DARK_PIG = registerDark("dark_pig", DarkPigEntity::new, PigEntity.createPigAttributes(), builder -> builder.setDimensions(0.9F, 0.9F));
	public static final EntityType<DarkChickenEntity> DARK_CHICKEN = registerDark("dark_chicken", DarkChickenEntity::new, ChickenEntity.createChickenAttributes(), builder -> builder.setDimensions(0.4F, 0.7F));
	public static final EntityType<CharacterEntity> CHARACTER = register("character", EntityType.Builder.create(CharacterEntity::new, SpawnGroup.MISC));

	public static void init() {}

	static <T extends LivingEntity> EntityType<T> registerGolem(String id, EntityType.EntityFactory<T> factory) {
		EntityType<T> type = register(id, EntityType.Builder.create(factory, SpawnGroup.MISC));
		FabricDefaultAttributeRegistry.register(type, LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE));
		return type;
	}

	static <T extends LivingEntity> EntityType<T> registerDark(String id, EntityType.EntityFactory<T> factory, DefaultAttributeContainer.Builder builder, UnaryOperator<EntityType.Builder<T>> sets) {
		EntityType<T> type = register(id, sets.apply(EntityType.Builder.create(factory, SpawnGroup.MONSTER)).maxTrackingRange(10));
		FabricDefaultAttributeRegistry.register(type, builder.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0));
		return type;
	}

	static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
		String cppId = Craftingpp.MOD_ID3 + ":" + id;
		return Registry.register(Registry.ENTITY_TYPE, cppId, type.build(cppId));
	}

}
