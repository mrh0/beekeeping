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
        CompoundTag beeTag = bee.getTag();
        CompoundTag tag = new CompoundTag();
        BeeItem.init(tag, type,
                LifetimeGene.get(beeTag),
                WeatherToleranceGene.get(beeTag),
                TemperatureToleranceGene.get(beeTag),
                LightToleranceGene.get(beeTag),
                RareProduceGene.get(beeTag));
        ItemStack r = new ItemStack(type);
        r.setTag(tag);
        return r;
    }

    public static int getByIndex(CompoundTag tag, int index) {
        return switch(index) {
            case 0 -> LifetimeGene.get(tag);
            case 1 -> WeatherToleranceGene.get(tag);
            case 2 -> TemperatureToleranceGene.get(tag);
            case 3 -> LightToleranceGene.get(tag);
            case 4 -> RareProduceGene.get(tag);
            default -> 0;
        };
    }

    public static void setByIndex(CompoundTag tag, int index, int value) {
        switch(index) {
            case 0:
                LifetimeGene.set(tag, value);
            case 1:
                WeatherToleranceGene.set(tag, value);
            case 2:
                TemperatureToleranceGene.set(tag, value);
            case 3:
                LightToleranceGene.set(tag, value);
            case 4:
                RareProduceGene.set(tag, value);
        }
    }

    public static ItemStack mutateRandom(ItemStack bee) {
        int selected = Gene.random.nextInt(0,5);
        boolean up = Gene.random.nextBoolean();

        return switch(selected) {
            case 0 -> up ? LifetimeGene.up(bee) : LifetimeGene.down(bee);
            case 1 -> up ? WeatherToleranceGene.up(bee) : WeatherToleranceGene.down(bee);
            case 2 -> up ? TemperatureToleranceGene.up(bee) : TemperatureToleranceGene.down(bee);
            case 3 -> up ? LightToleranceGene.up(bee) : LightToleranceGene.down(bee);
            case 4 -> up ? RareProduceGene.up(bee) : RareProduceGene.down(bee);
            default -> bee;
        };
    }

    public interface SelectFunction {
        int select(int a, int b);
    }
}
