package github.mrh0.beekeeping.bee.item;

import github.mrh0.beekeeping.bee.Specie;
import net.minecraft.resources.ResourceLocation;

public class PrincessBee extends BeeItem {
    private final ResourceLocation resource;
    public PrincessBee(Specie specie, Properties props, boolean foil) {
        super(specie, props, foil);
        resource = new ResourceLocation(specie.mod, specie.getName() + "_drone");
    }

    @Override
    public BeeType getType() {
        return BeeType.PRINCESS;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return resource;
    }
}
