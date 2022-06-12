package github.mrh0.beekeeping.bee.genes;

import net.minecraft.nbt.CompoundTag;

public enum RareProduceGene implements Gene {
    HIGHEST("highest"),
    HIGH("high"),
    NORMAL("normal"),
    LOW("low"),
    LOWEST("lowest");

    private final String name;

    RareProduceGene(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static void set(CompoundTag tag, int value) {
        Gene.set(tag, "prod", value);
    }

    public static int get(CompoundTag tag) {
        return Gene.get(tag, "prod");
    }

    public static RareProduceGene of(int value) {
        return switch (value) {
            case 0 -> HIGHEST;
            case 1 -> HIGH;
            case 3 -> LOW;
            case 4 -> LOWEST;
            default -> NORMAL;
        };
    }
}