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

    @Override
    public int getIndex() {
        return ordinal();
    }

    public static void set(CompoundTag tag, int value) {
        Gene.set(tag, "prod", value);
    }

    public static int get(CompoundTag tag) {
        return Gene.get(tag, "prod");
    }

    public static RareProduceGene of(int value) {
        return switch (value) {
            case 0 -> LOWEST;
            case 1 -> LOW;
            case 3 -> HIGH;
            case 4 -> HIGHEST;
            default -> NORMAL;
        };
    }
}