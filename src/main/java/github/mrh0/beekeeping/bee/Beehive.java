package github.mrh0.beekeeping.bee;

import github.mrh0.beekeeping.blocks.beehive.BeehiveBlock;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.function.Function;

public class Beehive {
    public final Specie specie;
    public final Function<Set<BiomeDictionary.Type>, Boolean> biomeType;
    public final int tries;
    public RegistryObject<BeehiveBlock> block;

    public Beehive(Specie specie, Function<Set<BiomeDictionary.Type>, Boolean> biomeType, int tries) {
        this.specie = specie;
        this.biomeType = biomeType;
        this.tries = tries;
    }

    public String getName() {
        return "beehive_" + specie.getName();
    }
}
