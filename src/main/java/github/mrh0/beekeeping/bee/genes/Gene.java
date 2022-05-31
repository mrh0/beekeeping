package github.mrh0.beekeeping.bee.genes;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;

import java.util.Random;

public interface Gene {
    static Random random = new Random();

    static int get(CompoundTag tag, String key) {
        return tag.getInt(key);
    }

    static void set(CompoundTag tag, String key, int value) {
        tag.putInt(key, value);
    }

    String getName();

    interface RandomFunction {
        int rand(Random rand);
    }

    static int randomWide(Random rand) {
        return rand.nextInt(5);
    }

    static int randomNarrow(Random rand) {
        return rand.nextInt(3) + 1;
    }

    static int randomLow(Random rand) {
        return rand.nextInt(3);
    }

    static int randomHigh(Random rand) {
        return rand.nextInt(3) + 2;
    }

    static int normal(Random rand) {
        return 2;
    }

    static int eval(RandomFunction fn) {
        return fn.rand(Gene.random);
    }

    static int select(int a, int b) {
        return Gene.random.nextBoolean() ? a : b;
    }
}
