package cpp.item;

import cpp.ducktyping.IPlayerWearing;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.world.World;

public class SnowBoots extends ArmorItem implements IPlayerWearing {
    public SnowBoots(Settings settings) {
        super(ArmorMaterials.LEATHER, EquipmentSlot.FEET, settings);
    }

    @Override
    public void playerWearing(World world, PlayerEntity player) {
        if (world.getBlockState(player.getBlockPos().down()).getBlock() == Blocks.SNOW_BLOCK)
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1, 0, false, false));
    }
}
