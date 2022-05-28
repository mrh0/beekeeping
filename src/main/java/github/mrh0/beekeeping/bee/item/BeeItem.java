package github.mrh0.beekeeping.bee.item;

import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.group.ItemGroup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class BeeItem extends Item {
    private final Specie specie;
    private final boolean foil;

    public BeeItem(Specie specie, Properties props, boolean foil) {
        super(props.tab(ItemGroup.BEES));
        this.specie = specie;
        this.foil = foil;
    }

    public enum BeeType {
        DRONE("drone"),
        PRINCESS("princess"),
        QUEEN("queen");
        public final String name;

        BeeType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    public boolean isFoil(ItemStack item) {
        return foil;
    }

    public abstract BeeType getType();

    public abstract ResourceLocation getResourceLocation();
}
