package cpp.block.entity;

import cpp.init.CppBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BrokenSpawnerBlockEntity extends BlockEntity {
    public BrokenSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(CppBlockEntities.BROKEN_SPAWNER, pos, state);
    }

    public void tick(World world, BlockPos pos) {
        double x = pos.getX() + world.random.nextDouble();
        double y = pos.getY() + world.random.nextDouble();
        double z = pos.getZ() + world.random.nextDouble();
        if (this.world.isPlayerInRange(pos.getX() + .5D, pos.getY() + .5D, pos.getZ() + .5D, 16)) {
            world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
