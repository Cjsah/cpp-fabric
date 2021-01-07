package net.cpp.entity.render;

import net.cpp.entity.GolemMinerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.Dilation;

@Environment(EnvType.CLIENT)
public class GolemMinerModel extends PlayerEntityModel<GolemMinerEntity> {

	public GolemMinerModel(ModelPart root) {
		super(root, false);
	}

	public static TexturedModelData getTexturedModelData() {
		return TexturedModelData.of(getTexturedModelData(Dilation.NONE, false), 64, 64);
	}
	
	static {
		
	}
}
