package cpp.ducktyping;

import cpp.vaccine.VaccineInstance;
import cpp.vaccine.Vaccines;

public interface IPlayerVaccine {

    void addVaccine(VaccineInstance vaccine);

    void removeVaccine(Vaccines vaccine);

    boolean containVaccine(Vaccines vaccine);
}
