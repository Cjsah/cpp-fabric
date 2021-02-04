package net.cpp.entity.render;

import net.cpp.entity.DarkChickenEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class DarkChickenEntityRenderer extends MobEntityRenderer<DarkChickenEntity, ChickenEntityModel<DarkChickenEntity>> {
	public static final Identifier TEXTURE = new Identifier("textures/entity/chicken.png");

	public DarkChickenEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new ChickenEntityModel<DarkChickenEntity>(context.getPart(EntityModelLayers.CHICKEN)), 0.7F);
	}

	public Identifier getTexture(DarkChickenEntity entity) {
		return TEXTURE;
	}

	protected float getAnimationProgress(DarkChickenEntity chickenEntity, float f) {
		float g = MathHelper.lerp(f, chickenEntity.prevFlapProgress, chickenEntity.flapProgress);
		float h = MathHelper.lerp(f, chickenEntity.prevMaxWingDeviation, chickenEntity.maxWingDeviation);
		return (MathHelper.sin(g) + 1.0F) * h;
	}
}
