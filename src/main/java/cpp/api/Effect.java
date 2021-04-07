package cpp.api;


import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class Effect extends StatusEffect {

    public Effect(StatusEffectType type, int color) {
        super(type, color);
    }
}
