package github.mrh0.beekeeping.bee;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.bee.item.DroneBee;
import github.mrh0.beekeeping.bee.item.PrincessBee;
import github.mrh0.beekeeping.bee.item.QueenBee;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class Specie {
    private final String name;
    private boolean foil;
    private final ResourceLocation resource;
    private final DroneBee droneItem;
    private final PrincessBee princessItem;
    private final QueenBee queenItem;
    public static String mod = Beekeeping.MODID;

    public Specie(String name, boolean foil) {
        this.name = name;
        this.foil = foil;
        this.resource = new ResourceLocation(mod, name);
        this.droneItem = buildDroneItem();
        this.princessItem = buildPrincessItem();
        this.queenItem = buildQueenItem();
    }

    public ResourceLocation getResourceLocation() {
        return resource;
    }

    public String getName() {
        return name;
    }

    protected DroneBee buildDroneItem() {
        return new DroneBee(this, new Item.Properties().stacksTo(1), foil);
    }

    protected PrincessBee buildPrincessItem() {
        return new PrincessBee(this, new Item.Properties().stacksTo(1), foil);
    }

    protected QueenBee buildQueenItem() {
        return new QueenBee(this, new Item.Properties().stacksTo(1), foil);
    }
}
