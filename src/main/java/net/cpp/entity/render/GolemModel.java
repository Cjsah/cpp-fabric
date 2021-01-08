package net.cpp.entity.render;

import net.cpp.entity.AGolemEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.Dilation;

public class GolemModel extends PlayerEntityModel<AGolemEntity> {

	public GolemModel(ModelPart root) {
		super(root, false);
	}
	public static TexturedModelData getTexturedModelData() {
		return TexturedModelData.of(getTexturedModelData(Dilation.NONE, false), 64, 64);
	}
}
