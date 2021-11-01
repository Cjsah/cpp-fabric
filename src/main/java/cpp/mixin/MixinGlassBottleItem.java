package cpp.mixin;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(GlassBottleItem.class)
public class MixinGlassBottleItem {
    @ModifyArg(at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getEntitiesByClass(Ljava/lang/Class;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"
    ), method = "use", index = 2)
    public <T extends AreaEffectCloudEntity> Predicate<? super T> modify(Predicate<? super T> predicate) {
        return entity -> entity != null && entity.isAlive() && (entity.getOwner() instanceof EnderDragonEntity || entity.getParticleType() == ParticleTypes.DRAGON_BREATH);
    }
}
