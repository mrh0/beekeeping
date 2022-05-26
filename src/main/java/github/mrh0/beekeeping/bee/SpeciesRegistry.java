package github.mrh0.beekeeping.bee;

import java.util.ArrayList;
import java.util.List;

public class SpeciesRegistry {
    private final static List<Specie> speciesList = new ArrayList<>();

    public Specie register(Specie specie) {
        speciesList.add(specie);
        return specie;
    }
}
