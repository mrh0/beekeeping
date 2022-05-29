package github.mrh0.beekeeping.bee.genes;

import net.minecraft.nbt.CompoundTag;

public enum LifetimeGene implements Gene {
    SHORTEST("shortest", 60*5),
    SHORT("short", 60*7),
    NORMAL("normal", 60*10),
    LONG("long", 60*13),
    LONGEST("longest", 60*15);

    private final String name;
    private final int time;

    LifetimeGene(String name, int time) {
        this.name = name;
        this.time = time;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public static void set(CompoundTag tag, int value) {
        Gene.set(tag, "lifetime", value);
    }

    public static int get(CompoundTag tag) {
        return Gene.get(tag, "lifetime");
    }

    public static LifetimeGene of(int value) {
        return switch(value) {
            case 0 -> SHORTEST;
            case 1 -> SHORT;
            case 3 -> LONG;
            case 4 -> LONGEST;
            default -> NORMAL;
        };
    }
}
