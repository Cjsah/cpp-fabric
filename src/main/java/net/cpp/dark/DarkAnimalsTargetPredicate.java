package net.cpp.dark;

import net.cpp.ducktyping.IPlayerVaccine;
import net.cpp.vaccine.Vaccines;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;

import javax.annotation.Nullable;

public class DarkAnimalsTargetPredicate extends TargetPredicate {

    @Override
    public boolean test(@Nullable LivingEntity baseEntity, LivingEntity targetEntity) {
        if (targetEntity instanceof IPlayerVaccine && ((IPlayerVaccine) targetEntity).containVaccine(Vaccines.DARKNESS)) return false;
        return super.test(baseEntity, targetEntity);
    }
}
