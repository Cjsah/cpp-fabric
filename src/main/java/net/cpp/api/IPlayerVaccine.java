package net.cpp.api;

public interface IPlayerVaccine {

    default void addVaccine(CppVaccine.Effect vaccine) {
        this.addVaccine(vaccine, vaccine.getDuration());
    }

    void addVaccine(CppVaccine.Effect vaccine, int duration);

    void removeVaccine(CppVaccine.Effect vaccine);
}
