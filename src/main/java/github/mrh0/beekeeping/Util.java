package github.mrh0.beekeeping;

import github.mrh0.beekeeping.bee.genes.Gene;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

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

    public static int getSunlight(Level level, BlockPos pos) {
        int i = level.getBrightness(LightLayer.SKY, pos) - level.getSkyDarken();
        return Mth.clamp(i, 0, 15);
    }
}
