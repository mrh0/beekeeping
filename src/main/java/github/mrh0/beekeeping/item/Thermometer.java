package github.mrh0.beekeeping.item;

import github.mrh0.beekeeping.group.ItemGroup;
import net.minecraft.world.item.Item;

public class Thermometer extends Item {
    public Thermometer() {
        super(new Properties().tab(ItemGroup.BEES).stacksTo(1));
    }
}
