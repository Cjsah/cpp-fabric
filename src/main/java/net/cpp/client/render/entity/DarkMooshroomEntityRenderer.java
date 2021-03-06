package net.cpp.client.render.entity;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.cpp.entity.DarkMooshroomEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DarkMooshroomEntityRenderer extends MobEntityRenderer<DarkMooshroomEntity, CowEntityModel<DarkMooshroomEntity>> {
	public static final Map<MooshroomEntity.Type, Identifier> TEXTURES = ImmutableMap.<MooshroomEntity.Type, Identifier>builder().put(MooshroomEntity.Type.BROWN, new Identifier("textures/entity/cow/brown_mooshroom.png")).put(MooshroomEntity.Type.RED, new Identifier("textures/entity/cow/red_mooshroom.png")).build();

	public DarkMooshroomEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new CowEntityModel<DarkMooshroomEntity>(context.getPart(EntityModelLayers.MOOSHROOM)), 0.7F);
	}

	public Identifier getTexture(DarkMooshroomEntity entity) {
		return TEXTURES.get(entity.getMooshroomType());
	}
}
