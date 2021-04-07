package cpp.client.render.entity;

import java.util.HashMap;
import java.util.Map;

import cpp.entity.AGolemEntity;
import cpp.entity.GolemFarmerEntity;
import cpp.entity.GolemFisherEntity;
import cpp.entity.GolemHerderEntity;
import cpp.entity.GolemMinerEntity;
import cpp.entity.GolemWarriorEntity;
import cpp.init.CppEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GolemEntityRenderer extends LivingEntityRenderer<AGolemEntity, GolemEntityModel> {
	public static final Map<EntityType<? extends AGolemEntity>, EntityModelLayer> LAYERS = new HashMap<>();
	public static final Map<Class<? extends AGolemEntity>, Identifier> TEXTURES = new HashMap<>();

	public GolemEntityRenderer(Context ctx, EntityType<? extends AGolemEntity> type) {
		super(ctx, new GolemEntityModel(ctx.getPart(LAYERS.get(type))), .5f);
	}

	@Override
	public Identifier getTexture(AGolemEntity entity) {
		return TEXTURES.get(entity.getClass());
	}

	static {
		for (EntityType<? extends AGolemEntity> type : CppEntities.GOLEMS)
			LAYERS.put(type, new EntityModelLayer(EntityType.getId(type), "main"));
		TEXTURES.put(GolemFarmerEntity.class, new Identifier("cpp:textures/entity/golem_farmer.png"));
		TEXTURES.put(GolemMinerEntity.class, new Identifier("cpp:textures/entity/golem_miner.png"));
		TEXTURES.put(GolemFisherEntity.class, new Identifier("cpp:textures/entity/golem_fisher.png"));
		TEXTURES.put(GolemWarriorEntity.class, new Identifier("cpp:textures/entity/golem_warrior.png"));
		TEXTURES.put(GolemHerderEntity.class, new Identifier("cpp:textures/entity/golem_herder.png"));
	}
}
