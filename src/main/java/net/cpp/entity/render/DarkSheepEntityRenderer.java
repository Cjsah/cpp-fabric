package net.cpp.entity.render;

import net.cpp.entity.DarkSheepEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.SheepEntityRenderer;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DarkSheepEntityRenderer extends MobEntityRenderer<DarkSheepEntity, DarkSheepEntityModel<DarkSheepEntity>> {
	public static final Identifier TEXTURE = new Identifier("textures/entity/sheep/sheep.png");

	public DarkSheepEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new DarkSheepEntityModel<DarkSheepEntity>(context.getPart(EntityModelLayers.SHEEP)), 0.7F);
		addFeature(new DarkSheepWoolFeatureRenderer(this, context.getModelLoader()));
	}

	public Identifier getTexture(DarkSheepEntity entity) {
		return TEXTURE;
	}
}