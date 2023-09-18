package github.mrh0.beekeeping.item.frame;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ProduceEvent {
    ItemStack trigger(Level level, BlockPos pos, ProduceType type, ItemStack produce);
    enum ProduceType {
        RARE,
        COMMON,
        QUEEN,
        PRINCESS,
        DRONE
    }
}
