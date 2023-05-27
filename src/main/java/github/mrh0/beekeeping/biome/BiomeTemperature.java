package github.mrh0.beekeeping.biome;

import github.mrh0.beekeeping.bee.genes.Gene;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

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
        BiomeTemperature r = TEMPERED;

        if(temp < 0.5f)
            r =  COLD;
        if(temp < 0f)
            r = COLDEST;
        if(temp > 1f)
            r =  WARM;
        if(temp > 1.5f)
            r =  WARMEST;
        return r;
    }

    public String getName() {
        return name;
    }

    public MutableComponent getComponent() {
        return Component.translatable("text.beekeeping.temperature." + getName()).withStyle(Gene.formatting[ordinal()]);
    }

    public boolean isAdjacent(BiomeTemperature temp) {
        return this == temp
            || this.ordinal()-1 == temp.ordinal()
            || this.ordinal()+1 == temp.ordinal();
    }
}
