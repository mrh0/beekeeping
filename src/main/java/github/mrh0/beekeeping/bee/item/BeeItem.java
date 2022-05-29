package github.mrh0.beekeeping.bee.item;

import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.genes.Gene;
import github.mrh0.beekeeping.bee.genes.LifetimeGene;
import github.mrh0.beekeeping.group.ItemGroup;
import net.minecraft.nbt.CompoundTag;
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

    public static void setAnalyzed(CompoundTag tag, boolean analyzed) {
        tag.putBoolean("analyzed", analyzed);
    }

    public static boolean getAnalyzed(CompoundTag tag) {
        return tag.getBoolean("analyzed");
    }

    public static void setHealth(CompoundTag tag, int health) {
        tag.putInt("health", health);
    }

    public static int getHealth(CompoundTag tag) {
        return tag.getInt("health");
    }

    public static void init(ItemStack stack) {
        if(stack.getTag() == null)
            stack.setTag(new CompoundTag());
        CompoundTag tag = stack.getTag();
        setAnalyzed(tag, true);
        LifetimeGene.set(tag, Gene.randomWide());
        setHealth(tag, LifetimeGene.of(LifetimeGene.get(tag)).getTime());
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if(stack.getTag() == null)
            return false;
        return getAnalyzed(stack.getTag());
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return super.getBarWidth(stack);
    }

    public boolean isFoil() {
        return foil;
    }
}
