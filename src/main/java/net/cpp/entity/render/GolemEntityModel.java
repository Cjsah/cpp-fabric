package net.cpp.entity.render;

import net.cpp.entity.AGolemEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.Dilation;

@Environment(EnvType.CLIENT)
public class GolemEntityModel extends PlayerEntityModel<AGolemEntity> {

	public GolemEntityModel(ModelPart root) {
		super(root, false);
	}

	public static TexturedModelData getTexturedModelData() {
		return TexturedModelData.of(getTexturedModelData(Dilation.NONE, false), 64, 64);
	}
}
