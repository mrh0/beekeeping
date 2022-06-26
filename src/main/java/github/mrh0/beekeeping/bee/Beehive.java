package github.mrh0.beekeeping.bee;

import github.mrh0.beekeeping.blocks.beehive.BeehiveBlock;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.function.Function;

public class Beehive {
    public final Specie specie;
    public final Function<Set<BiomeDictionary.Type>, Boolean> biomeType;
    public final int tries;
    public final int rarity;
    public final PlacementModifier modifier;
    public RegistryObject<BeehiveBlock> block;

    public Beehive(Specie specie, Function<Set<BiomeDictionary.Type>, Boolean> biomeType, int tries, int rarity) {
        this.specie = specie;
        this.biomeType = biomeType;
        this.tries = tries;
        this.rarity = rarity;
        this.modifier = PlacementUtils.HEIGHTMAP;
    }

    public Beehive(Specie specie, Function<Set<BiomeDictionary.Type>, Boolean> biomeType, int tries, int rarity, PlacementModifier modifier) {
        this.specie = specie;
        this.biomeType = biomeType;
        this.tries = tries;
        this.rarity = rarity;
        this.modifier = modifier;
    }

    public String getName() {
        return specie.getName() + "_beehive";
    }
}
