package github.mrh0.beekeeping.screen.slot;

import github.mrh0.beekeeping.Index;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BeeSlot extends SlotItemHandler {
    public BeeSlot(IItemHandler itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(Index.BEES_TAG);
    }
}