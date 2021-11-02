package cpp.client.render.entity;

import cpp.Craftingpp;
import cpp.entity.CharacterEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class CharacterEntityRender extends EntityRenderer<CharacterEntity> {
    public CharacterEntityRender(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(CharacterEntity entity) {
        return new Identifier(Craftingpp.MOD_ID3, "textures/character/" + entity.getCharacterID() + ".png");
    }

    @Override
    public void render(CharacterEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
        matrices.scale(0.5F, 0.5F, 0.5F);

        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(this.getTexture(entity)));
        MatrixStack.Entry matrixStack$entry = matrices.peek();
        Matrix4f matrix4f = matrixStack$entry.getModel();
        Matrix3f matrix3f = matrixStack$entry.getNormal();
        // front
        this.drawVertex(matrix4f, matrix3f, buffer, 0, -2, -1, 0.0F, 1.0F, -1, 0, 0, light);
        this.drawVertex(matrix4f, matrix3f, buffer, 0, -2, 1, 1.0F, 1.0F, -1, 0, 0, light);
        this.drawVertex(matrix4f, matrix3f, buffer, 0, 2, 1, 1.0F, 0.0F, -1, 0, 0, light);
        this.drawVertex(matrix4f, matrix3f, buffer, 0, 2, -1, 0.0F, 0.0F, -1, 0, 0, light);
        // back
        this.drawVertex(matrix4f, matrix3f, buffer, 0, 2, -1, 0.0F, 0.0F, 1, 0, 0, light);
        this.drawVertex(matrix4f, matrix3f, buffer, 0, 2, 1, 1.0F, 0.0F, 1, 0, 0, light);
        this.drawVertex(matrix4f, matrix3f, buffer, 0, -2, 1, 1.0F, 1.0F, 1, 0, 0, light);
        this.drawVertex(matrix4f, matrix3f, buffer, 0, -2, -1, 0.0F, 1.0F, 1, 0, 0, light);

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer builder, int offsetX, int offsetY, int offsetZ, float u, float v, int normalX, int normalY, int normalZ, int light) {
        builder.vertex(matrix, offsetX, offsetY, offsetZ).color(255, 255, 255, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normals, normalX, normalY, normalZ).next();
    }

}
