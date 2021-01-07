package net.cpp.entity.render;

import net.cpp.entity.GolemMinerEntity;
import net.cpp.init.CppEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GolemMinerRenderer extends LivingEntityRenderer<GolemMinerEntity, GolemMinerModel> {
	public static final EntityModelLayer MAIN_LAYER = new EntityModelLayer(EntityType.getId(CppEntities.GOLEM_MINER), "main");
	public static final Identifier TEXTURE = new Identifier("cpp:textures/entity/golem_miner.png");
	public GolemMinerRenderer(Context ctx) {
		super(ctx, new GolemMinerModel(ctx.getPart(MAIN_LAYER)), .5f);
	}

	@Override
	public Identifier getTexture(GolemMinerEntity entity) {
		return TEXTURE;
	}
}
