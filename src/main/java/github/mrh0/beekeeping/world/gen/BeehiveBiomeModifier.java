package github.mrh0.beekeeping.world.gen;

import com.mojang.serialization.Codec;
import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.world.feature.BeekeepingPlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder;

public record BeehiveBiomeModifier() implements BiomeModifier {

    @Override
    public void modify(Holder<Biome> biome, Phase phase, Builder builder) {
        if (phase != Phase.ADD) {
            return;
        }
        // TODO: this is probably not ideal and we should probably move all the beehive placement into data jsons akin to:
        // https://github.com/MinecraftForge/MinecraftForge/blob/1.19.x/src/generated_test/resources/data/biome_modifiers_test/forge/biome_modifier/add_basalt.json
        for (Specie species : SpeciesRegistry.instance.getAll()) {
            if (!species.hasBeehive()) {
                continue;
            }

            if (species.beehive.acceptsBiome(biome)) {
                var placedFeature = BeekeepingPlacedFeatures.getPlacedFeatures(species.beehive, species.beehive.rarity, species.beehive.modifier, species.beehive.feature);
                builder.getGenerationSettings().addFeature(Decoration.VEGETAL_DECORATION, placedFeature);
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return Index.BEEHIVE_BIOME_MODIFIER_CODEC.get();
    }
}
