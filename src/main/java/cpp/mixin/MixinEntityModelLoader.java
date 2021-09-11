package cpp.mixin;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cpp.client.render.entity.GolemEntityModel;
import cpp.client.render.entity.GolemEntityRenderer;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.resource.ResourceManager;

@Mixin(EntityModelLoader.class)
public class MixinEntityModelLoader {
	@Shadow
	private Map<EntityModelLayer, TexturedModelData> modelParts;

	@Inject(at = @At("RETURN"), method = "reload")
	public void reload(ResourceManager manager, CallbackInfo info) {
		this.modelParts = new HashMap<>(this.modelParts);
		for (EntityModelLayer layer : GolemEntityRenderer.LAYERS.values())
			this.modelParts.put(layer, GolemEntityModel.getTexturedModelData());
	}
}
