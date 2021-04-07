package cpp.dark;

import cpp.ducktyping.IPlayerVaccine;
import cpp.vaccine.Vaccines;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.mob.MobEntity;

public class DarkAnimalsFollowTargetGoal<T extends LivingEntity> extends FollowTargetGoal<T> {

    public DarkAnimalsFollowTargetGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility) {
        super(mob, targetClass, checkVisibility);
        this.targetPredicate = (new DarkAnimalsTargetPredicate()).setBaseMaxDistance(this.getFollowRange()).setPredicate(null);
    }

    @Override
    public boolean shouldContinue() {
        if (this.targetEntity instanceof IPlayerVaccine && ((IPlayerVaccine) this.targetEntity).containVaccine(Vaccines.DARKNESS)) return false;
        return super.shouldContinue();
    }
}
