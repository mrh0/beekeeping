package github.mrh0.beekeeping.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IFrameBreedingEvent {
    void trigger(Level level, BlockPos pos, ItemStack queen);
}
