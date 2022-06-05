package github.mrh0.beekeeping.bee.genes;

import net.minecraft.nbt.CompoundTag;

public enum ProduceBalanceGene implements Gene {
    STRICT("strict"),
    PICKY("picky"),
    ANY("any");

    private final String name;

    ProduceBalanceGene(String name) {
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

    public static ProduceBalanceGene of(int value) {
        return switch (value) {
            case 0 -> STRICT;
            case 1 -> PICKY;
            default -> ANY;
        };
    }
}