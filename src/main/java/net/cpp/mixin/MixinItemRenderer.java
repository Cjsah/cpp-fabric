package net.cpp.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.cpp.init.CppItems;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.SynchronousResourceReloadListener;
import net.minecraft.util.Formatting;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer implements SynchronousResourceReloadListener {

	@Shadow
	public float zOffset;

	@Inject(at = { @At("RETURN") }, method = { "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V" })
	private void renderGuiItemOverlay(TextRenderer renderer, ItemStack stack, int x, int y, @Nullable String countLabel, CallbackInfo info) {
		if (stack.isOf(CppItems.COMPRESSED_ITEM) || stack.isOf(Items.EXPERIENCE_BOTTLE)) {
			MatrixStack matrixStack = new MatrixStack();
			int multiple = stack.getOrCreateTag().getByte("multiple");
			if (multiple != 0) {
				String string = String.valueOf(multiple);
				matrixStack.translate(0.0D, 0.0D, zOffset + 200);
				VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
				renderer.draw((String) string, (float) (x + 19 - 2 - renderer.getWidth(string)), (float) (y + 0), Formatting.DARK_AQUA.getColorValue()|0xff000000, true, matrixStack.peek().getModel(), immediate, false, 0, 15728880);
				immediate.draw();
			}
		}
	}
}
