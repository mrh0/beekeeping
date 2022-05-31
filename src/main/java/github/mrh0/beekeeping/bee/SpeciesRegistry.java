package github.mrh0.beekeeping.bee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeciesRegistry {
    public static final SpeciesRegistry instance = new SpeciesRegistry();
    private final List<Specie> speciesList = new ArrayList<>();
    private final Map<String, Specie> speciesMap = new HashMap<>();

    public Specie register(Specie specie) {
        speciesList.add(specie);
        speciesMap.put(specie.getName(), specie);
        return specie;
    }

    public Specie get(String name) {
        return speciesMap.get(name);
    }

    public Specie get(int index) {
        return speciesList.get(index);
    }

    public List<Specie> getAll() {
        return speciesList;
    }
}
