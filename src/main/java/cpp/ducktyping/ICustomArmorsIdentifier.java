package cpp.ducktyping;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface ICustomArmorsIdentifier {
    // 材质
    @Nullable
    @Environment(EnvType.CLIENT)
    String getArmorIdentifier(Entity entity, ItemStack stack, EquipmentSlot slot);
}
