package net.cpp.entity.render;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import net.cpp.entity.GolemMinerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.Dilation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GolemMinerModel extends PlayerEntityModel<GolemMinerEntity> {
//	public static final ModelPart ROOT = new ModelPart(ImmutableList.of(new ModelPart.Cuboid(6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, false, 256, 256)), Collections.emptyMap());

	public GolemMinerModel(ModelPart root) {
		super(root, false);
		// TODO 自动生成的构造函数存根
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		// TODO 自动生成的方法存根
//		head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	public static TexturedModelData getTexturedModelData() {
//	      ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0.0F);
		return TexturedModelData.of(getTexturedModelData(Dilation.NONE, false), 64, 64);
	}
	
	static {
		
	}
}
