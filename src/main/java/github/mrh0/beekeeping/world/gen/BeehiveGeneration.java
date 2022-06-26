package github.mrh0.beekeeping.world.gen;

import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.world.feature.BeekeepingPlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;
import java.util.Set;

public class BeehiveGeneration {
    public static void generateSurfaceBeehives(final BiomeLoadingEvent event) {
        ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);

        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            if (!specie.hasBeehive())
                continue;

            if(specie.beehive.biomeType.apply(types)) {
                List<Holder<PlacedFeature>> base =
                        event.getGeneration().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION);
                base.add(BeekeepingPlacedFeatures.getPlacedFeatures(specie.beehive, specie.beehive.rarity, PlacementUtils.HEIGHTMAP));
            }
        }
    }
}