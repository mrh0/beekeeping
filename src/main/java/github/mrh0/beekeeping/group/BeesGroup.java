package github.mrh0.beekeeping.group;

import github.mrh0.beekeeping.Beekeeping;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BeesGroup extends CreativeModeTab {
    public static BeesGroup BEES;

    public BeesGroup() {
        super(Beekeeping.MODID+":bees");
        BEES = this;
    }
    @Override
    public net.minecraft.world.item.ItemStack makeIcon() {
        return new ItemStack(Items.BEE_NEST);
    }
}
