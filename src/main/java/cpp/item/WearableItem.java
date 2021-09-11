package cpp.item;

import cpp.Craftingpp;
import cpp.ducktyping.ICustomArmorsIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.item.ItemExtensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WearableItem extends Item implements Wearable, ICustomArmorsIdentifier {
    private final boolean hasArmorIdentifier;
    public WearableItem(EquipmentSlot equipmentSlot, boolean hasArmorIdentifier, Settings settings) {
        super(settings);
        ((ItemExtensions)this).fabric_setEquipmentSlotProvider(stack -> equipmentSlot);
        this.hasArmorIdentifier = hasArmorIdentifier;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(itemStack);
        ItemStack itemStack2 = user.getEquippedStack(equipmentSlot);
        if (itemStack2.isEmpty()) {
            user.equipStack(equipmentSlot, itemStack.copy());
            if (!world.isClient()) {
                user.incrementStat(Stats.USED.getOrCreateStat(this));
            }
            itemStack.setCount(0);
            return TypedActionResult.success(itemStack, world.isClient());
        } else {
            return TypedActionResult.fail(itemStack);
        }

    }

    @Override
    @Nullable
    @Environment(EnvType.CLIENT)
    public String getArmorIdentifier(Entity entity, ItemStack stack, EquipmentSlot slot) {
        if (this.hasArmorIdentifier) return new Identifier(Craftingpp.MOD_ID3, String.format("textures/entity/armor/%s.png", this.getTranslationKey().split("\\.")[2])).toString();
        return null;
    }
}
