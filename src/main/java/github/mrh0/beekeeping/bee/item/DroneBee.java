package github.mrh0.beekeeping.bee.item;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Specie;
import net.minecraft.resources.ResourceLocation;

public class DroneBee extends BeeItem {
    private final ResourceLocation resource;
    public DroneBee(Specie specie, Properties props, boolean foil) {
        super(specie, props, foil);
        resource = new ResourceLocation(Beekeeping.MODID, specie.getName() + "_drone");
    }

    @Override
    public BeeType getType() {
        return BeeType.DRONE;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return resource;
    }
}
