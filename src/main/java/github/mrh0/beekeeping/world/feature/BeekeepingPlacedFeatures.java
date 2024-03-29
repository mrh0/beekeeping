package github.mrh0.beekeeping.world.feature;

import github.mrh0.beekeeping.bee.Beehive;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.*;

public class BeekeepingPlacedFeatures {
    public static Holder<PlacedFeature> getPlacedFeatures(Beehive beehive, int rarity, PlacementModifier placement, Feature feature) {
        return PlacementUtils.register(beehive.getName() + "_placed",
                BeekeepingConfiguredFeatures.getConfiguredFeatures(beehive, feature), RarityFilter.onAverageOnceEvery(rarity),
                InSquarePlacement.spread(), placement, BiomeFilter.biome());
    }
}