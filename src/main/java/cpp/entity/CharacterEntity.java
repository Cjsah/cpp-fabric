package cpp.entity;

import net.fabricmc.fabric.api.network.PacketRegistry;
import net.fabricmc.fabric.impl.networking.ServerSidePacketRegistryImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class CharacterEntity extends Entity {
    private int character;

    public CharacterEntity(EntityType<?> type, World world) {
        super(type, world);
        this.character = -1;
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("character")) this.character = nbt.getInt("character");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("character", this.character);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        ServerSidePacketRegistryImpl
        return null;
    }
}
