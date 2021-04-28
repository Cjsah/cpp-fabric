package cpp.mixin;

import cpp.api.IDefaultNbtItem;
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
		if (item != null && item instanceof IDefaultNbtItem) {
			NbtCompound nbt = this.getOrCreateTag();
			NbtCompound newNbt = ((IDefaultNbtItem)item).modifyDefaultNbt(new NbtCompound());
			for (String i : newNbt.getKeys()){
				if (!nbt.contains(i)) {
					nbt.put(i, newNbt.get(i));
				}
			}
		}
	}
}