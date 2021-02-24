package net.cpp.api;

import net.cpp.vaccine.VaccineInstance;
import net.cpp.vaccine.Vaccines;

public interface IPlayerVaccine {

    void addVaccine(VaccineInstance vaccine);

    void removeVaccine(Vaccines vaccine);
}
