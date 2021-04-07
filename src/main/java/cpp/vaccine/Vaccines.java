package cpp.vaccine;

import javax.annotation.Nullable;

public enum Vaccines {
    POISON("minecraft:poison", 216000),
    BLINDNESS("minecraft:blindness", 216000),
    MINING_FATIGUE("minecraft:mining_fatigue", 216000),
    WITHER("minecraft:wither", 216000),
    DARKNESS("cpp:darkness", 864000);

    private final String name;
    private final int duration;

    Vaccines(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public static Vaccines byRawId(byte rawId) {
        try {
            return Vaccines.values()[rawId];
        }catch (ArrayIndexOutOfBoundsException exception) {
            return null;
        }
    }
}
