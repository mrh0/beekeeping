package github.mrh0.beekeeping.bee.genes;

import github.mrh0.beekeeping.config.Config;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public enum LifetimeGene implements Gene {
    SHORTEST("shortest", Config.LIFETIME_GENE_SHORTEST.get()),
    SHORT("short", Config.LIFETIME_GENE_SHORT.get()),
    NORMAL("normal", Config.LIFETIME_GENE_NORMAL.get()),
    LONG("long", Config.LIFETIME_GENE_LONG.get()),
    LONGEST("longest", Config.LIFETIME_GENE_LONGEST.get());

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

    @Override
    public int getIndex() {
        return ordinal();
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

    public static ItemStack up(ItemStack bee) {
        int now = get(bee.getTag());
        set(bee.getTag(), Math.min(now+1, 4));
        return bee;
    }

    public static ItemStack down(ItemStack bee) {
        int now = get(bee.getTag());
        set(bee.getTag(), Math.max(now+1, 0));
        return bee;
    }
}
