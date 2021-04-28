package cpp.vaccine;

import net.minecraft.nbt.NbtCompound;

import javax.annotation.Nonnull;

public class VaccineInstance {

    private final Vaccines vaccine;
    private int duration;

    public VaccineInstance(Vaccines vaccine) {
        this(vaccine, vaccine.getDuration());
    }

    public VaccineInstance(Vaccines vaccine, int duration) {
        this.vaccine = vaccine;
        this.duration = duration;
    }

    public VaccineInstance copy() {
        return new VaccineInstance(this.vaccine, this.duration);
    }

    public Vaccines getVaccine() {
        return vaccine;
    }

    public int getDuration() {
        return duration;
    }

    public boolean updateDuration() {
        return --this.duration > 0;
    }

    public NbtCompound writeNbt(@Nonnull NbtCompound nbt) {
        nbt.putByte("Id", (byte) this.vaccine.ordinal());
        nbt.putInt("Duration", this.duration);
        return nbt;
    }

    @Nonnull
    public static VaccineInstance fromNbt(@Nonnull NbtCompound nbt) {
        return new VaccineInstance(Vaccines.byRawId(nbt.getByte("Id")), nbt.getInt("Duration"));
    }
}
