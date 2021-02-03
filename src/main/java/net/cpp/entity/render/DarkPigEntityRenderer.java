package net.cpp.entity.render;

import net.cpp.entity.DarkPigEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class DarkPigEntityRenderer extends MobEntityRenderer<DarkPigEntity, PigEntityModel<DarkPigEntity>> {
	public static final Identifier TEXTURE = new Identifier("textures/entity/pig/pig.png");

	public DarkPigEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new PigEntityModel<DarkPigEntity>(context.getPart(EntityModelLayers.PIG)), 0.7F);
	}

	public Identifier getTexture(DarkPigEntity entity) {
		return TEXTURE;
	}

	protected void scale(DarkPigEntity darkPig, MatrixStack matrixStack, float f) {
		float g = darkPig.getClientFuseTime(f);
		float h = 1.0F + MathHelper.sin(g * 100.0F) * g * 0.01F;
		g = MathHelper.clamp(g, 0.0F, 1.0F);
		g *= g;
		g *= g;
		float i = (1.0F + g * 0.4F) * h;
		float j = (1.0F + g * 0.1F) / h;
		matrixStack.scale(i, j, i);
	}

	protected float getAnimationCounter(DarkPigEntity darkPig, float f) {
		float g = darkPig.getClientFuseTime(f);
		return (int) (g * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(g, 0.5F, 1.0F);
	}
}
