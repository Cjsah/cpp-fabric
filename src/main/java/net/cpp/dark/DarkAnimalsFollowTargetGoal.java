package net.cpp.dark;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;

import java.util.EnumSet;

public class DarkAnimalsFollowTargetGoal<T extends LivingEntity> extends TrackTargetGoal {
    protected final Class<T> targetClass;
    protected final int reciprocalChance = 10;
    protected LivingEntity targetEntity;
    protected TargetPredicate targetPredicate;

    public DarkAnimalsFollowTargetGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility) {
        super(mob, checkVisibility, false);
        this.targetClass = targetClass;
        this.setControls(EnumSet.of(Control.TARGET));
        this.targetPredicate = (new DarkAnimalsTargetPredicate()).setBaseMaxDistance(this.getFollowRange()).setPredicate(null);

    }

    @Override
    public boolean canStart() {
        if (this.mob.getRandom().nextInt(this.reciprocalChance) != 0) {
            return false;
        } else {
            this.findClosestTarget();
            return this.targetEntity != null;
        }
    }

    protected Box getSearchBox(double distance) {
        return this.mob.getBoundingBox().expand(distance, 4.0D, distance);
    }

    protected void findClosestTarget() {
        if (this.targetClass != PlayerEntity.class && this.targetClass != ServerPlayerEntity.class) {
            this.targetEntity = this.mob.world.getClosestEntity(this.mob.world.getEntitiesByClass(this.targetClass, this.getSearchBox(this.getFollowRange()), (livingEntity) -> true), this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        } else {
            this.targetEntity = this.mob.world.getClosestPlayer(this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }

    }

    @Override
    public void start() {
        this.mob.setTarget(this.targetEntity);
        super.start();
    }
}
