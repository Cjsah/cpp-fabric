package net.cpp.client.render.block.entity;

import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class VariantSkullBlockEntityModel extends Model {
	public VariantSkullBlockEntityModel() {
		super(RenderLayer::getEntityTranslucent);
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
	
	}
}
