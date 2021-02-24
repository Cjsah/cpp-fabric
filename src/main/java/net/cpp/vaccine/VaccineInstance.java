package net.cpp.vaccine;

import net.minecraft.nbt.CompoundTag;

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

    public CompoundTag toTag(CompoundTag tag) {
        tag.putByte("Id", this.vaccine.getRawId());
        tag.putInt("Duration", this.duration);
        return tag;
    }

    public static VaccineInstance fromTag(CompoundTag tag) {
        return new VaccineInstance(Vaccines.byRawId(tag.getByte("Id")), tag.getInt("Duration"));
    }
}
