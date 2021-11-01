package cpp.mixin;

import cpp.Craftingpp;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.ModelIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cpp.init.CppItems;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;

@Mixin(ItemModels.class)
public abstract class MixinItemModels {
	@Shadow
	public abstract BakedModel getModel(ItemStack stack);

	@Shadow
	public abstract BakedModelManager getModelManager();

	@Inject(at = { @At("HEAD") }, method = "getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;", cancellable = true)
	public void getModelCpp(ItemStack stack, CallbackInfoReturnable<BakedModel> info) {
		if (stack.isOf(CppItems.COMPRESSED_ITEM)) {
			info.setReturnValue(getModel(ItemStack.fromNbt(stack.getOrCreateSubTag("item"))));
		}else if (stack.isOf(CppItems.CHARACTER)) {
//			ModelIdentifier model = new ModelIdentifier(Craftingpp.MOD_ID3, "character/" + stack.getOrCreateTag().getInt("character"), "inventory");
//			System.out.println(model);
//			info.setReturnValue(this.getModelManager().getModel(model));
		}
	}
}
