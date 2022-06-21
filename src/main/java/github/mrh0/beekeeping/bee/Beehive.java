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
    public final int rarity;
    public RegistryObject<BeehiveBlock> block;

    public Beehive(Specie specie, Function<Set<BiomeDictionary.Type>, Boolean> biomeType, int tries, int rarity) {
        this.specie = specie;
        this.biomeType = biomeType;
        this.tries = tries;
        this.rarity = rarity;
    }

    public String getName() {
        return specie.getName() + "_beehive";
    }
}
