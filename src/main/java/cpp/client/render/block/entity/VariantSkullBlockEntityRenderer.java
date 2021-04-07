package cpp.client.render.block.entity;

import com.google.common.collect.ImmutableMap;
import cpp.Craftingpp;
import cpp.block.VariantSkullBlock;
import cpp.block.entity.VariantSkullBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Map;

public class VariantSkullBlockEntityRenderer implements BlockEntityRenderer<VariantSkullBlockEntity> {
	public final SkullBlockEntityModel model;
	public static final Map<String, Identifier> IDENTIFIER_MAP = ImmutableMap.<String, Identifier>builder().build();
	
	public VariantSkullBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		model = new SkullEntityModel(ctx.getLayerRenderDispatcher().getModelPart(EntityModelLayers.SKELETON_SKULL));
	}
	
	@Override
	public void render(VariantSkullBlockEntity skullBlockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		BlockState blockState = skullBlockEntity.getCachedState();
		float yaw = 22.5F * blockState.get(VariantSkullBlock.ROTATION);
		RenderLayer renderLayer = RenderLayer.getEntityCutoutNoCullZOffset(IDENTIFIER_MAP.getOrDefault(skullBlockEntity.getTextureName(),new Identifier("")));
		matrices.push();
		matrices.translate(0.5D, blockState.get(VariantSkullBlock.WALL) ? 0.25 : 0, 0.5D);
		matrices.scale(-1.0F, -1.0F, 1.0F);
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
		model.setHeadRotation(0, yaw, 0.0F);
		model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		matrices.pop();
	}
}
