package net.cpp.mixin;

import net.cpp.item.IDefaultTagItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	@Shadow
	private Item item;

	@Shadow
	public abstract CompoundTag getOrCreateTag();

	@Inject(at = @At("HEAD"), method = "updateEmptyState()V")
	private void updateEmptyState(CallbackInfo info) {
		if (item != null && item instanceof IDefaultTagItem) {
			CompoundTag tag = this.getOrCreateTag();
			CompoundTag newTag = ((IDefaultTagItem)item).modifyDefaultTag(new CompoundTag());
			for (String i : newTag.getKeys()){
				if (!tag.contains(i)) {
					tag.put(i, newTag.get(i));
				}
			}
		}
	}
}