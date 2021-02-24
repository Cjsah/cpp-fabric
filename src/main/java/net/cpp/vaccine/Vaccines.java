package net.cpp.vaccine;

import javax.annotation.Nullable;

public enum Vaccines {
    POISON("minecraft:poison", (byte) 0, 200),
    BLINDNESS("minecraft:blindness", (byte) 1, 200),
    MINING_FATIGUE("minecraft:mining_fatigue", (byte) 2, 200),
    WITHER("minecraft:wither", (byte) 3, 200),
    DARKNESS("cpp:darkness", (byte) 4, 200);

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
