package net.cpp.vaccine;

import javax.annotation.Nullable;

public enum Vaccines {
    POISON("minecraft:poison", (byte) 0, 216000),
    BLINDNESS("minecraft:blindness", (byte) 1, 216000),
    MINING_FATIGUE("minecraft:mining_fatigue", (byte) 2, 216000),
    WITHER("minecraft:wither", (byte) 3, 216000),
    DARKNESS("cpp:darkness", (byte) 4, 864000);

    private final byte rawId;
    private final String name;
    private final int duration;

    Vaccines(String name, byte rawId, int duration){
        this.rawId = rawId;
        this.name = name;
        this.duration = duration;
    }

    public byte getRawId() {
        return rawId;
    }

    public int getDuration() {
        return this.duration;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public static Vaccines byRawId(byte rawId) {
        for (Vaccines vaccine : Vaccines.values()) {
            if (rawId == vaccine.rawId) return vaccine;
        }
        return null;
    }
}
