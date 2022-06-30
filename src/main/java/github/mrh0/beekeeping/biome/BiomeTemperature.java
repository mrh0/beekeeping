package github.mrh0.beekeeping.biome;

import github.mrh0.beekeeping.bee.genes.Gene;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

public enum BiomeTemperature {
    COLDEST("coldest"),
    COLD("cold"),
    TEMPERED("tempered"),
    WARM("warm"),
    WARMEST("warmest");

    private String name;

    BiomeTemperature(String name) {
        this.name = name;
    }

    public static BiomeTemperature of(float temp) {
        if(temp < 0f)
            return COLDEST;
        else if(temp < 0.5f)
            return COLD;
        else if(temp > 1f)
            return WARM;
        else if(temp > 1.5f)
            return WARMEST;
        return TEMPERED;
    }

    public String getName() {
        return name;
    }

    public MutableComponent getComponent() {
        return new TranslatableComponent("text.beekeeping.temperature." + getName()).withStyle(Gene.formatting[ordinal()]);
    }

    public boolean isAdjacent(BiomeTemperature temp) {
        return this == temp
            || this.ordinal()-1 == temp.ordinal()
            || this.ordinal()+1 == temp.ordinal();
    }
}
