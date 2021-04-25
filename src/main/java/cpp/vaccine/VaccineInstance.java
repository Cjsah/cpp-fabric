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

    public NbtCompound toTag(@Nonnull NbtCompound tag) {
        tag.putByte("Id", (byte) this.vaccine.ordinal());
        tag.putInt("Duration", this.duration);
        return tag;
    }

    @Nonnull
    public static VaccineInstance fromTag(@Nonnull NbtCompound tag) {
        return new VaccineInstance(Vaccines.byRawId(tag.getByte("Id")), tag.getInt("Duration"));
    }
}
