package cpp.entity;

import cpp.api.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
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
    protected int getXpToDrop(PlayerEntity player) {
        return super.getXpToDrop(player) + 5;
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
            if (this.transform) Utils.darkTransform((ServerWorld) this.world, this, this.transformedEntity, false, null);
        }
    }

    public void setTransform(boolean transform) {
        this.transform = transform;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("TransForm", this.transform);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.transform = nbt.getBoolean("TransForm");
    }
}
