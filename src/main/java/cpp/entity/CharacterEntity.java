package cpp.entity;

import cpp.init.CppItems;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CharacterEntity extends Entity {
    private static final TrackedData<Integer> CHARACTER_ID = DataTracker.registerData(CharacterEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Direction> FACING = DataTracker.registerData(CharacterEntity.class, TrackedDataHandlerRegistry.FACING);

    public CharacterEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public int getCharacterID() {
        return this.dataTracker.get(CHARACTER_ID);
    }

    @Override
    public void tick() {
        super.tick();

        BlockState state = this.world.getBlockState(this.getBlockPos());

//        if (!BlockTags.BEDS.contains(state.getBlock()) || state.get(BedBlock.PART) != BedPart.FOOT) {
//            ItemStack item = new ItemStack(CppItems.CHARACTER);
//            item.getOrCreateTag().putInt("character", this.character);
//            ItemEntity entity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), item);
//            this.world.spawnEntity(entity);
//        }

    }

    @Override
    protected Box calculateBoundingBox() {
        Vec3d pos = this.getPos();
        return new Box(pos.x - 1.0D, pos.y, pos.z - 0.5D, pos.x + 1.0D, pos.y + 0.01D, pos.z + 0.5D);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(CHARACTER_ID, -1);
        this.dataTracker.startTracking(FACING, Direction.NORTH);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.dataTracker.set(CHARACTER_ID, nbt.getInt("character"));
        this.dataTracker.set(FACING, Direction.byName(nbt.getString("facing")));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("character", this.dataTracker.get(CHARACTER_ID));
        nbt.putString("facing", this.dataTracker.get(FACING).asString());
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}
