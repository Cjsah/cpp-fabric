package net.cpp.entity.render;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.cpp.entity.GolemMinerEntity;
import net.cpp.init.CppEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PiglinEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GolemMinerRenderer extends LivingEntityRenderer<GolemMinerEntity, GolemMinerModel> {
	public static final EntityModelLayer MAIN_LAYER = new EntityModelLayer(EntityType.getId(CppEntities.GOLEM_MINER), "main");
	public static final Identifier TEXTURE = new Identifier("cpp:textures/entity/golem_miner.png");
	public GolemMinerRenderer(Context ctx) {
		super(ctx, new GolemMinerModel(ctx.getPart(MAIN_LAYER)), .5f);
		// TODO 自动生成的构造函数存根
	}

	@Override
	public Identifier getTexture(GolemMinerEntity entity) {
		return TEXTURE;
	}
}
