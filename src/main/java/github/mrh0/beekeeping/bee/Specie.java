package github.mrh0.beekeeping.bee;

import com.mojang.datafixers.util.Pair;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Util;
import github.mrh0.beekeeping.bee.genes.*;
import github.mrh0.beekeeping.bee.item.DroneBee;
import github.mrh0.beekeeping.bee.item.PrincessBee;
import github.mrh0.beekeeping.bee.item.QueenBee;
import github.mrh0.beekeeping.biome.BiomeTemperature;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Specie {
    private final String name;
    private boolean foil = false;
    private int color;
    private boolean dark = false;
    private boolean hasRareProduce = false;
    //private TagKey<Biome>[] preferredBiomes = null;
    private final ResourceLocation resource;
    public DroneBee droneItem;
    public PrincessBee princessItem;
    public QueenBee queenItem;
    public static String mod = Beekeeping.MODID;

    public Gene.RandomFunction lifetimeGene = Gene::random5Narrow;
    public Gene.RandomFunction weatherGene = Gene::strict;
    public Gene.RandomFunction temperatureGene = Gene::picky;
    public Gene.RandomFunction lightGene = Gene::strict;
    public Gene.RandomFunction produceGene = Gene::random5Narrow;

    public boolean isNocturnal = false;
    public BiomeTemperature preferredTemperature = BiomeTemperature.TEMPERED;

    public Beehive beehive = null;
    public Produce produce = null;

    public List<Pair<String, String>> breeding;

    public Specie(String name, int color) {
        this.name = name;
        this.color = color;
        this.resource = new ResourceLocation(mod, name);
        this.breeding = new ArrayList<>();
    }

    public ResourceLocation getResourceLocation() {
        return resource;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public DroneBee buildDroneItem() {
        this.droneItem = new DroneBee(this, new Item.Properties().stacksTo(1), foil);
        return this.droneItem;
    }

    public PrincessBee buildPrincessItem() {
        this.princessItem = new PrincessBee(this, new Item.Properties().stacksTo(1), foil);
        return this.princessItem;
    }

    public QueenBee buildQueenItem() {
        this.queenItem = new QueenBee(this, new Item.Properties().stacksTo(1), foil);
        return this.queenItem;
    }

    public Specie setWeatherGene(Gene.RandomFunction fn) {
        this.weatherGene = fn;
        return this;
    }

    public Specie setTemperatureGene(Gene.RandomFunction fn) {
        this.temperatureGene = fn;
        return this;
    }

    public Specie setLightGene(Gene.RandomFunction fn) {
        this.lightGene = fn;
        return this;
    }

    public Specie setProduceGene(Gene.RandomFunction fn) {
        this.produceGene = fn;
        return this;
    }

    public Specie setLifetimeGene(Gene.RandomFunction fn) {
        this.lifetimeGene = fn;
        return this;
    }

    /*@Deprecated
    public Specie setPreferredBiomes(TagKey...biomeTag) {
        this.preferredBiomes = biomeTag;
        return this;
    }*/

    public Specie setPreferredTemperature(BiomeTemperature temp) {
        preferredTemperature = temp;
        return this;
    }

    public Specie ofBiome(Biome biome) {
        preferredTemperature = BiomeTemperature.of(biome.getBaseTemperature());
        weatherGene = rand -> biome.getDownfall() < 0.5f ? WeatherToleranceGene.STRICT.ordinal() : WeatherToleranceGene.PICKY.ordinal();
        return this;
    }

    public boolean hasSatisfactoryLightLevel(int sunlight, boolean isDay) {
        if(isNocturnal)
            return sunlight > 11 && !isDay;
        return sunlight > 11 && isDay;
    }

    public Specie setNocturnal() {
        isNocturnal = true;
        return this;
    }

    /*public Satisfaction getBiomeSatisfaction(ItemStack stack, Level level, BlockPos pos) {
        //level.getBiomeManager().getBiome(pos).value().getBaseTemperature();
        if(preferredBiomes == null || preferredBiomes.length == 0)
            return Satisfaction.SATISFIED;
        for(TagKey<Biome> tag : preferredBiomes) {
            if(level.getBiomeManager().getBiome(pos).is(tag))
                return Satisfaction.SATISFIED;
        }
        BiomeToleranceGene tolerance = BiomeToleranceGene.of(BiomeToleranceGene.get(stack.getTag()));
        return switch(tolerance) {
            case PICKY -> Satisfaction.UNSATISFIED;
            case STRICT -> Satisfaction.NOT_WORKING;
            default -> Satisfaction.SATISFIED;
        };
        //return level.getBiomeManager().getBiome(pos).is(preferredBiomes) ? Satisfaction.SATISFIED : Satisfaction.NOT_WORKING;
    }*/

    public Satisfaction getLightSatisfaction(ItemStack stack, Level level, BlockPos pos) {
        LightToleranceGene tolerance = LightToleranceGene.of(LightToleranceGene.get(stack.getTag()));
        int sunlight = Util.getSunlight(level, pos);

        return switch (tolerance) {
            case PICKY -> hasSatisfactoryLightLevel(sunlight, level.isDay()) ? Satisfaction.SATISFIED : Satisfaction.UNSATISFIED;
            case STRICT -> hasSatisfactoryLightLevel(sunlight, level.isDay()) ? Satisfaction.SATISFIED : Satisfaction.NOT_WORKING;
            default -> Satisfaction.SATISFIED;
        };
    }

    public Satisfaction getWeatherSatisfaction(ItemStack stack, Level level, BlockPos pos) {
        WeatherToleranceGene tolerance = WeatherToleranceGene.of(WeatherToleranceGene.get(stack.getTag()));
        if(level.isThundering())
            return Satisfaction.NOT_WORKING;

        return switch (tolerance) {
            case PICKY -> level.isRaining() ? Satisfaction.UNSATISFIED : Satisfaction.SATISFIED;
            case STRICT -> level.isRaining() ? Satisfaction.NOT_WORKING : Satisfaction.SATISFIED;
            default -> Satisfaction.SATISFIED;
        };
    }

    public Satisfaction getTemperatureSatisfaction(ItemStack stack, Level level, BlockPos pos) {
        float f = level.getBiomeManager().getBiome(pos).value().getBaseTemperature();
        BiomeTemperature temp = BiomeTemperature.of(f);

        TemperatureToleranceGene tolerance = TemperatureToleranceGene.of(TemperatureToleranceGene.get(stack.getTag()));

        return switch (tolerance) {
            case PICKY -> preferredTemperature == temp ? Satisfaction.SATISFIED : (preferredTemperature.isAdjacent(temp) ? Satisfaction.UNSATISFIED : Satisfaction.NOT_WORKING);
            case STRICT -> preferredTemperature == temp ? Satisfaction.SATISFIED : Satisfaction.NOT_WORKING;
            default -> Satisfaction.SATISFIED;
        };
    }

    public Specie addBeehive(Function<Set<BiomeDictionary.Type>, Boolean> biomeType, int tries, int rarity) {
        this.beehive = new Beehive(this, biomeType, tries, rarity);
        return this;
    }

    public Specie addBeehive(Function<Set<BiomeDictionary.Type>, Boolean> biomeType, int tries, int rarity, PlacementModifier modifier, Feature feature, Function<BlockPos, Boolean> blockPlaceAllow) {
        this.beehive = new Beehive(this, biomeType, tries, rarity, modifier, feature, blockPlaceAllow);
        return this;
    }

    public Specie setProduce(Item common, int commonCountUnsatisfied, int commonCountSatisfied) {
        this.produce = new Produce(common, commonCountUnsatisfied, commonCountSatisfied, null, 0, 0, 0, 0);
        return this;
    }

    public Specie setProduce(Item common, int commonCountUnsatisfied, int commonCountSatisfied, Item rare,
                             double rareChanceUnsatisfied, double rareChanceSatisfied) {
        this.produce = new Produce(common, commonCountUnsatisfied, commonCountSatisfied,
                rare, 1, 1, rareChanceUnsatisfied, rareChanceSatisfied);
        hasRareProduce = true;
        return this;
    }

    public Specie setProduce(Item common, int commonCountUnsatisfied, int commonCountSatisfied, Item rare, int rareCountUnsatisfied, int rareCountSatisfied,
                             double rareChanceUnsatisfied, double rareChanceSatisfied) {
        this.produce = new Produce(common, commonCountUnsatisfied, commonCountSatisfied,
                rare, rareCountUnsatisfied, rareCountSatisfied, rareChanceUnsatisfied, rareChanceSatisfied);
        hasRareProduce = true;
        return this;
    }

    public Specie setProduce(Item common, int commonCountUnsatisfied, int commonCountSatisfied, Item rare, int rareCountUnsatisfied, int rareCountSatisfied) {
        this.produce = new Produce(common, commonCountUnsatisfied, commonCountSatisfied,
                rare, rareCountUnsatisfied, rareCountSatisfied, 1d, 1d);
        hasRareProduce = true;
        return this;
    }

    public Specie breedFrom(String bee1, String bee2) {
        breeding.add(new Pair<>(bee1, bee2));
        breeding.add(new Pair<>(bee2, bee1));
        return this;
    }

    public Specie breedStrictFrom(String drone, String princess) {
        breeding.add(new Pair<>(drone, princess));
        return this;
    }

    public Specie setDark() {
        this.dark = true;
        return this;
    }

    public Specie setFoil() {
        this.foil = true;
        return this;
    }

    public boolean isDark() {
        return this.dark;
    }

    public boolean isHasRareProduce() {
        return this.hasRareProduce;
    }

    public boolean hasBeehive() {
        return this.beehive != null;
    }

    public static Specie getByName(String name) {
        return SpeciesRegistry.instance.get(name);
    }

    public static Specie getByIndex(int index) {
        return SpeciesRegistry.instance.get(index);
    }
}
