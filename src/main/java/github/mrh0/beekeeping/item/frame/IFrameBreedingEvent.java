package github.mrh0.beekeeping.item.frame;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IFrameBreedingEvent {
    ItemStack trigger(Level level, BlockPos pos, ItemStack drone, ItemStack princess, ItemStack queen);
}
