package github.mrh0.beekeeping.world.feature;

import github.mrh0.beekeeping.bee.Beehive;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class BeekeepingConfiguredFeatures {
    public static Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> getConfiguredFeatures(Beehive beehive) {
        return FeatureUtils.register(beehive.getName(), Feature.FLOWER,
                new RandomPatchConfiguration(beehive.tries, 6, 1, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(beehive.specie.beehive.block.get())))));
    }
    //public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> BEEHIVE =

}