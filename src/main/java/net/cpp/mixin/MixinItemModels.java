package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.cpp.init.CppItems;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;

@Mixin(ItemModels.class)
@SuppressWarnings("unused")
public abstract class MixinItemModels {
	@Shadow
	public abstract BakedModel getModel(ItemStack stack);

	@Inject(at = { @At("HEAD") }, method = { "getModel" }, cancellable = true)
	private void getModelCpp(ItemStack stack, CallbackInfoReturnable<BakedModel> info) {
		if (stack.isOf(CppItems.COMPRESSED_ITEM)) {
			info.setReturnValue(getModel(ItemStack.fromTag(stack.getOrCreateSubTag("item"))));
		}
	}
}
