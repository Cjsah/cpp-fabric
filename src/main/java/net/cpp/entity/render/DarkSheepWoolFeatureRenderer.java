package net.cpp.entity.render;

import net.cpp.entity.DarkSheepEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.client.render.entity.model.SheepWoolEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DarkSheepWoolFeatureRenderer extends FeatureRenderer<DarkSheepEntity, DarkSheepEntityModel<DarkSheepEntity>> {
	private static final Identifier SKIN = new Identifier("textures/entity/sheep/sheep_fur.png");
	private final DarkSheepWoolEntityModel<DarkSheepEntity> model;

	public DarkSheepWoolFeatureRenderer(FeatureRendererContext<DarkSheepEntity, DarkSheepEntityModel<DarkSheepEntity>> featureRendererContext, EntityModelLoader entityModelLoader) {
		super(featureRendererContext);
		this.model = new DarkSheepWoolEntityModel<DarkSheepEntity>(entityModelLoader.getModelPart(EntityModelLayers.SHEEP_FUR));
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, DarkSheepEntity sheepEntity, float f, float g, float h, float j, float k, float l) {
		if (!sheepEntity.isInvisible()) {
			float v;
			float w;
			float x;
			float[] hs = SheepEntity.getRgbColor(DyeColor.WHITE);
			v = hs[0];
			w = hs[1];
			x = hs[2];
			render(this.getContextModel(), this.model, SKIN, matrixStack, vertexConsumerProvider, i, sheepEntity, f, g, j, k, l, h, v, w, x);
		}
	}
}
