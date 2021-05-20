package cpp.dark;

import cpp.ducktyping.IPlayerVaccine;
import cpp.vaccine.Vaccines;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;


public class DarkAnimalsLookAtEntityGoal extends LookAtEntityGoal {

    protected final TargetPredicate targetPredicate;

    public DarkAnimalsLookAtEntityGoal(MobEntity mob, Class<? extends LivingEntity> targetType, float range) {
        super(mob, targetType, range);
        if (targetType == PlayerEntity.class) {
            this.targetPredicate = (new DarkAnimalsTargetPredicate()).setBaseMaxDistance(range).setPredicate((livingEntity) -> EntityPredicates.rides(mob).test(livingEntity));
        } else {
            this.targetPredicate = (new DarkAnimalsTargetPredicate()).setBaseMaxDistance(range);
        }
    }

    @Override
    public boolean shouldContinue() {
        if (this.target instanceof IPlayerVaccine && ((IPlayerVaccine) this.target).containVaccine(Vaccines.DARKNESS)) return false;
        return super.shouldContinue();
    }
}
