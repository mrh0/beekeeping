package github.mrh0.beekeeping.bee;

import java.util.ArrayList;
import java.util.List;

public class SpeciesRegistry {
    public static final SpeciesRegistry instance = new SpeciesRegistry();
    private final List<Specie> speciesList = new ArrayList<>();

    public Specie register(Specie specie) {
        speciesList.add(specie);
        return specie;
    }

    public List<Specie> getAll() {
        return speciesList;
    }
}
