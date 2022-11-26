package github.mrh0.beekeeping.bee.breeding;

import github.mrh0.beekeeping.Util;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.genes.*;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.recipe.BeeBreedingRecipe;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class BeeLifecycle {
    public static Specie getOffspringSpecie(Level level, ItemStack drone, ItemStack princess) {
        SimpleContainer inv = new SimpleContainer(2);
        inv.setItem(0, drone);
        inv.setItem(1, princess);

        Optional<BeeBreedingRecipe> match = level.getRecipeManager()
                .getRecipeFor(BeeBreedingRecipe.Type.INSTANCE, inv, level);
        if(match.isEmpty())
            return Util.selectRandom(BeeItem.speciesOf(drone), BeeItem.speciesOf(princess));
        return match.get().getOffspring();
    }

    public static CompoundTag getOffspringTag(ItemStack drone, ItemStack princess, Specie offspring, SelectFunction fn) {
        CompoundTag tag = new CompoundTag();
        BeeItem.init(tag, offspring.queenItem,
                fn.select(LifetimeGene.get(drone.getTag()), LifetimeGene.get(princess.getTag())),
                fn.select(WeatherToleranceGene.get(drone.getTag()), WeatherToleranceGene.get(princess.getTag())),
                fn.select(TemperatureToleranceGene.get(drone.getTag()), TemperatureToleranceGene.get(princess.getTag())),
                fn.select(LightToleranceGene.get(drone.getTag()), LightToleranceGene.get(princess.getTag())),
                fn.select(RareProduceGene.get(drone.getTag()), RareProduceGene.get(princess.getTag()))
        );
        return tag;
    }

    public static Optional<BeeProduceRecipe> getProduceRecipe(Level level, ItemStack queen) {
        SimpleContainer inv = new SimpleContainer(1);
        inv.setItem(0, queen);

        return level.getRecipeManager()
                .getRecipeFor(BeeProduceRecipe.Type.INSTANCE, inv, level);
    }

    public static ItemStack clone(ItemStack bee, BeeItem type) {
        CompoundTag b = bee.getTag();
        CompoundTag tag = new CompoundTag();
        BeeItem.init(tag, type,
                LifetimeGene.get(b),
                WeatherToleranceGene.get(b),
                TemperatureToleranceGene.get(b),
                LightToleranceGene.get(b),
                RareProduceGene.get(b));
        ItemStack r = new ItemStack(type);
        r.setTag(tag);
        return r;
    }

    public interface SelectFunction {
        int select(int a, int b);
    }
}
