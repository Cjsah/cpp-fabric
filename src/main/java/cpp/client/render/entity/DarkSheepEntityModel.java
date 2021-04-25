package cpp.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class DarkSheepEntityModel<T extends Entity> extends QuadrupedEntityModel<T> {
	private float headPitchModifier;

	public DarkSheepEntityModel(ModelPart root) {
		super(root, false, 8.0F, 4.0F, 2.0F, 2.0F, 24);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = QuadrupedEntityModel.getModelData(12, Dilation.NONE);
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -4.0F, -6.0F, 6.0F, 6.0F, 8.0F), ModelTransform.pivot(0.0F, 6.0F, -8.0F));
		modelPartData.addChild("body", ModelPartBuilder.create().uv(28, 8).cuboid(-4.0F, -10.0F, -7.0F, 8.0F, 16.0F, 6.0F), ModelTransform.of(0.0F, 5.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 32);
	}

	public void setAngles(T sheepEntity, float f, float g, float h, float i, float j) {
		super.setAngles(sheepEntity, f, g, h, i, j);
		this.head.pitch = this.headPitchModifier;
	}
}
