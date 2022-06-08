package github.mrh0.beekeeping.bee;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.bee.item.DroneBee;
import github.mrh0.beekeeping.bee.item.PrincessBee;
import github.mrh0.beekeeping.bee.item.QueenBee;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class Specie {
    private final String name;
    private boolean foil;
    private int color;
    private TagKey<Biome> preferredBiome = null;
    private final ResourceLocation resource;
    public DroneBee droneItem;
    public PrincessBee princessItem;
    public QueenBee queenItem;
    public static String mod = Beekeeping.MODID;

    public Gene.RandomFunction lifetimeGene = Gene::randomNarrow;
    public Gene.RandomFunction environmentGene = Gene::normal;
    public Gene.RandomFunction lightGene = Gene::normal;
    public Gene.RandomFunction produceGene = Gene::normal;

    public enum Satisfaction {
        SATISFIED(0, "tooltip.beekeeping.apiary.satisfied"),
        UNSATISFIED(1, "tooltip.beekeeping.apiary.unsatisfied"),
        NOT_WORKING(2, "tooltip.beekeeping.apiary.not_working");

        public final int index;
        public final TranslatableComponent component;

        Satisfaction(int index, String key) {
            this.index = index;
            this.component = new TranslatableComponent(key);
        }

        public static Satisfaction calc(Satisfaction...list) {
            Satisfaction satisfaction = SATISFIED;
            for(Satisfaction s : list) {
                if(s == NOT_WORKING)
                    return NOT_WORKING;
                if(s == UNSATISFIED)
                    satisfaction = UNSATISFIED;
            }
            return satisfaction;
        }
    }

    public Specie(String name, int color, boolean foil) {
        this.name = name;
        this.foil = foil;
        this.color = color;
        this.resource = new ResourceLocation(mod, name);
    }

    public Specie(String name, int color) {
        this(name, color, false);
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

    public Specie setEnvironmentGene(Gene.RandomFunction fn) {
        this.environmentGene = fn;
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

    public Specie setPreferredBiome(TagKey<Biome> biomeTag) {
        this.preferredBiome = biomeTag;
        return this;
    }

    public Satisfaction getBiomeSatisfaction(ItemStack stack, Level level, BlockPos pos) {
        //level.getBiomeManager().getBiome(pos).value().getBaseTemperature();
        if(preferredBiome == null)
            return Satisfaction.SATISFIED;
        return level.getBiomeManager().getBiome(pos).is(preferredBiome) ? Satisfaction.SATISFIED : Satisfaction.NOT_WORKING;
    }

    public static Specie getByName(String name) {
        return SpeciesRegistry.instance.get(name);
    }

    public static Specie getByIndex(int index) {
        return SpeciesRegistry.instance.get(index);
    }
}
