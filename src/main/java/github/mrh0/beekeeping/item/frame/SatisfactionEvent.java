package github.mrh0.beekeeping.item.frame;

import github.mrh0.beekeeping.bee.Satisfaction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface SatisfactionEvent {
    Satisfaction trigger(Level level, BlockPos pos, SatisfactionType type, ItemStack queen, Satisfaction satisfaction);
    enum SatisfactionType {
        LIGHT,
        WEATHER,
        TEMPERATURE,
        TOTAL
    }
}
