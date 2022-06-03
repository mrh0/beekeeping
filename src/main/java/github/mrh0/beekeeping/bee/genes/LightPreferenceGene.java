package github.mrh0.beekeeping.bee.genes;

import net.minecraft.nbt.CompoundTag;

public enum LightPreferenceGene implements Gene {
    NOCTURNAL("nocturnal"),
    DARK("dark"),
    ANY("any"),
    LIGHT("light"),
    SUNNY("sunny");

    private final String name;

    LightPreferenceGene(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static void set(CompoundTag tag, int value) {
        Gene.set(tag, "light", value);
    }

    public static int get(CompoundTag tag) {
        return Gene.get(tag, "light");
    }

    public static LightPreferenceGene of(int value) {
        return switch (value) {
            case 0 -> NOCTURNAL;
            case 1 -> DARK;
            case 3 -> LIGHT;
            case 4 -> SUNNY;
            default -> ANY;
        };
    }
}