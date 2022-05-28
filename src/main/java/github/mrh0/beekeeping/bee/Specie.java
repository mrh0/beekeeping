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
    private int color;
    private final ResourceLocation resource;
    public DroneBee droneItem;
    public PrincessBee princessItem;
    public QueenBee queenItem;
    public static String mod = Beekeeping.MODID;

    public Specie(String name, int color, boolean foil) {
        this.name = name;
        this.foil = foil;
        this.color = color;
        this.resource = new ResourceLocation(mod, name);
    }

    public Specie(String name, int color) {
        this(name, color, false);
    }

    public ResourceLocation getResourceLocation() {
        return resource;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public DroneBee buildDroneItem() {
        this.droneItem = new DroneBee(this, new Item.Properties().stacksTo(1), foil);
        return this.droneItem;
    }

    public PrincessBee buildPrincessItem() {
        this.princessItem = new PrincessBee(this, new Item.Properties().stacksTo(1), foil);
        return this.princessItem;
    }

    public QueenBee buildQueenItem() {
        this.queenItem = new QueenBee(this, new Item.Properties().stacksTo(1), foil);
        return this.queenItem;
    }
}
