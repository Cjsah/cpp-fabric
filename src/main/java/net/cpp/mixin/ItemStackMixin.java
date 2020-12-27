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
	private CompoundTag tag;

	@Inject(at = @At("HEAD"), method = "updateEmptyState()V")
	private void updateEmptyState(CallbackInfo info) {
		if (item != null && item instanceof IDefaultTagItem) {
			appendTag(((IDefaultTagItem)item).getDefaultTag());
		}
	}

	private void appendTag(CompoundTag tag) {
		CompoundTag newTag;
		if (this.tag == null) {
			newTag = new CompoundTag();
		} else {
			newTag = this.tag;
		}
		for (String name : tag.getKeys()) {
			if (!newTag.contains(name)) {
				newTag.put(name, tag.get(name));
			}
		}
		this.tag = newTag;
	}
}