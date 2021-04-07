package cpp.client.render.entity;

import cpp.entity.DarkCowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DarkCowEntityRenderer extends MobEntityRenderer<DarkCowEntity, CowEntityModel<DarkCowEntity>> {
	public static final Identifier TEXTURE = new Identifier("textures/entity/cow/cow.png");

	public DarkCowEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new CowEntityModel<DarkCowEntity>(context.getPart(EntityModelLayers.COW)), 0.7F);
	}

	public Identifier getTexture(DarkCowEntity entity) {
		return TEXTURE;
	}
}
