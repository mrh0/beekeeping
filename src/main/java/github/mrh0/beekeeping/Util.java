package github.mrh0.beekeeping;

import github.mrh0.beekeeping.bee.genes.Gene;
import net.minecraft.world.item.ItemStack;

public class Util {
    public static String capitalize(String str) {
        if(str == null || str.isEmpty())
            return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static <T> T selectRandom(T...items) {
        return items[Gene.random.nextInt(items.length)];
    }

    public static ItemStack rollChance(ItemStack stack, double chance) {
        if(Gene.random.nextDouble() < chance)
            return stack;
        return ItemStack.EMPTY;
    }
}
