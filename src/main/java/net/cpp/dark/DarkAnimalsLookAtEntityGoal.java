package net.cpp.dark;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;

import java.util.EnumSet;

public class DarkAnimalsLookAtEntityGoal extends Goal {

    protected final MobEntity mob;
    protected Entity target;
    protected final float range;
    private int lookTime;
    protected final float chance = 0.2F;
    protected final Class<? extends LivingEntity> targetType;
    protected final TargetPredicate targetPredicate;

    public DarkAnimalsLookAtEntityGoal(MobEntity mob, Class<? extends LivingEntity> targetType, float range) {
        this.mob = mob;
        this.targetType = targetType;
        this.range = range;
        this.setControls(EnumSet.of(Control.LOOK));
        if (targetType == PlayerEntity.class) {
            this.targetPredicate = (new DarkAnimalsTargetPredicate()).setBaseMaxDistance(range).includeTeammates().includeInvulnerable().ignoreEntityTargetRules().setPredicate((livingEntity) -> EntityPredicates.rides(mob).test(livingEntity));
        } else {
            this.targetPredicate = (new DarkAnimalsTargetPredicate()).setBaseMaxDistance(range).includeTeammates().includeInvulnerable().ignoreEntityTargetRules();
        }
    }

    @Override
    public boolean canStart() {
        if (this.mob.getRandom().nextFloat() >= this.chance) {
            return false;
        } else {
            if (this.mob.getTarget() != null) {
                this.target = this.mob.getTarget();
            }

            if (this.targetType == PlayerEntity.class) {
                this.target = this.mob.world.getClosestPlayer(this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
            } else {
                this.target = this.mob.world.getClosestEntity(this.mob.world.getEntitiesByClass(this.targetType, this.mob.getBoundingBox().expand((double)this.range, 3.0D, (double)this.range), (livingEntity) -> true), this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
            }

            return this.target != null;
        }
    }

    @Override
    public boolean shouldContinue() {
        if (!this.target.isAlive()) {
            return false;
        } else if (this.mob.squaredDistanceTo(this.target) > (double)(this.range * this.range)) {
            return false;
        } else {
            return this.lookTime > 0;
        }
    }

    @Override
    public void start() {
        this.lookTime = 40 + this.mob.getRandom().nextInt(40);
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public void tick() {
        this.mob.getLookControl().lookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
        --this.lookTime;
    }

}
