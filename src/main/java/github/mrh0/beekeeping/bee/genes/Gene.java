package github.mrh0.beekeeping.bee.genes;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;

import java.util.Random;

public interface Gene {
    static Random rand = new Random();

    static int get(CompoundTag tag, String key) {
        return tag.getInt(key);
    }

    static void set(CompoundTag tag, String key, int value) {
        tag.putInt(key, value);
    }

    String getName();

    static int randomWide() {
        return rand.nextInt(5);
    }

    static int randomNarrow() {
        return rand.nextInt(3) + 1;
    }

    static int randomLow() {
        return rand.nextInt(3);
    }

    static int randomHigh() {
        return rand.nextInt(3) + 2;
    }
}
