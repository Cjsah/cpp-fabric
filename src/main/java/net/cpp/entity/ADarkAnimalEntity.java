package net.cpp.entity;

import net.cpp.api.CodingTool;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public abstract class ADarkAnimalEntity<T extends Entity> extends HostileEntity {
    protected boolean transform = true;
    protected final EntityType<T> transformedEntity;

    protected ADarkAnimalEntity(EntityType<? extends HostileEntity> entityType, World world, EntityType<T> transformedEntity) {
        super(entityType, world);
        this.transformedEntity = transformedEntity;
    }

    @Override
    protected int getCurrentExperience(PlayerEntity player) {
        return super.getCurrentExperience(player) + 5;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isClient) {
            this.world.addParticle(ParticleTypes.SMOKE, getX(), getEyeY(), getZ(),
                    (this.world.random.nextFloat() - .5f) / 10,
                    this.world.random.nextFloat() / 10,
                    (this.world.random.nextFloat() - .5f) / 10);
        } else {
            if (this.transform) CodingTool.darkTransform((ServerWorld) this.world, this, this.transformedEntity, false);
        }
    }

    public void setTransform(boolean transform) {
        this.transform = transform;
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putBoolean("TransForm", this.transform);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.transform = tag.getBoolean("TransForm");
    }
}
