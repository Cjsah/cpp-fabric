package cpp.mixin;

import cpp.api.IDefaultTagItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
@SuppressWarnings("unused")
public abstract class MixinItemStack {

	@Shadow
	private Item item;

	@Shadow
	public abstract NbtCompound getOrCreateTag();

	@Inject(at = @At("HEAD"), method = "updateEmptyState()V")
	private void updateEmptyState(CallbackInfo info) {
		if (item != null && item instanceof IDefaultTagItem) {
			NbtCompound tag = this.getOrCreateTag();
			NbtCompound newTag = ((IDefaultTagItem)item).modifyDefaultTag(new NbtCompound());
			for (String i : newTag.getKeys()){
				if (!tag.contains(i)) {
					tag.put(i, newTag.get(i));
				}
			}
		}
	}
}