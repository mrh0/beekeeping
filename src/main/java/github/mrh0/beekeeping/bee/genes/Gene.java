package github.mrh0.beekeeping.bee.genes;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Random;

public interface Gene {
    static Random random = new Random();

    static int get(CompoundTag tag, String key) {
        if(tag == null)
            return 0;
        return tag.getInt(key);
    }

    static void set(CompoundTag tag, String key, int value) {
        tag.putInt(key, value);
    }

    String getName();

    int getIndex();

    interface RandomFunction {
        int rand(Random rand);
    }

    static int random5Wide(Random rand) {
        return rand.nextInt(5);
    }

    static int random5Narrow(Random rand) {
        return rand.nextInt(3) + 1;
    }

    static int random5Low(Random rand) {
        return rand.nextInt(3);
    }

    static int random5High(Random rand) {
        return rand.nextInt(3) + 2;
    }

    static int normal5(Random rand) {
        return 2;
    }

    static int strict(Random rand) {
        return 0;
    }
    static int picky(Random rand) {
        return 1;
    }
    static int any(Random rand) {
        return 2;
    }

    static int random3Low(Random rand) {
        return rand.nextInt(2);
    }

    static int random3High(Random rand) {
        return rand.nextInt(2) + 1;
    }

    static int eval(RandomFunction fn) {
        return fn.rand(Gene.random);
    }

    static int select(int a, int b) {
        return Gene.random.nextBoolean() ? a : b;
    }

    ChatFormatting[] formatting = {
            ChatFormatting.DARK_AQUA,
            ChatFormatting.AQUA,
            ChatFormatting.YELLOW,
            ChatFormatting.GOLD,
            ChatFormatting.RED
    };

    default MutableComponent getComponent() {
        return new TranslatableComponent("text.beekeeping.gene.type." + getName()).withStyle(formatting[getIndex()]);
    }
}
