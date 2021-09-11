package cpp.mixin;

import cpp.ducktyping.ICustomArmorsIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.item.ItemExtensions;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(ArmorFeatureRenderer.class)
public abstract class MixinArmorFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    public MixinArmorFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Final
    @Shadow
    private static Map<String, Identifier> ARMOR_TEXTURE_CACHE;

    @Shadow
    protected abstract void setVisible(A bipedModel, EquipmentSlot slot);

    @Inject(at = @At("HEAD"), method = "renderArmor")
    private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot slot, int light, A model, CallbackInfo ci) {
        ItemStack itemStack = entity.getEquippedStack(slot);
        if (itemStack.getItem() instanceof ICustomArmorsIdentifier item && ((ItemExtensions)item).fabric_getEquipmentSlotProvider().getPreferredEquipmentSlot(itemStack) == slot) {
            String armorIdentifier = item.getArmorIdentifier(entity, itemStack, slot);
            if (armorIdentifier != null) {
                this.getContextModel().setAttributes(model);
                this.setVisible(model, slot);
                Identifier texture = ARMOR_TEXTURE_CACHE.computeIfAbsent(armorIdentifier, Identifier::new);
                VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(texture), false, itemStack.hasGlint());
                model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
