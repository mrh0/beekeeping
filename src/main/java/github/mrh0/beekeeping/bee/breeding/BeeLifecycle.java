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
            return Util.selectRandom(BeeItem.of(drone), BeeItem.of(princess));
        return match.get().getOffspring();
    }

    public static CompoundTag getOffspringItemStack(ItemStack drone, ItemStack princess, Specie offspring) {
        CompoundTag tag = new CompoundTag();
        BeeItem.init(tag, offspring.queenItem,
                Gene.select(LifetimeGene.get(drone.getTag()), LifetimeGene.get(princess.getTag())),
                Gene.select(BiomeToleranceGene.get(drone.getTag()), BiomeToleranceGene.get(princess.getTag())),
                Gene.select(LightToleranceGene.get(drone.getTag()), LightToleranceGene.get(princess.getTag())),
                Gene.select(RareProduceGene.get(drone.getTag()), RareProduceGene.get(princess.getTag()))
        );
        return tag;
    }

    public static Optional<BeeProduceRecipe> getProduceRecipe(Level level, ItemStack queen) {
        SimpleContainer inv = new SimpleContainer(1);
        inv.setItem(0, queen);

        return level.getRecipeManager()
                .getRecipeFor(BeeProduceRecipe.Type.INSTANCE, inv, level);
    }
}
