package github.mrh0.beekeeping.datagen;

import com.mojang.datafixers.util.Pair;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import github.mrh0.beekeeping.bee.item.BeeItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider implements IConditionBuilder {
    public RecipeGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> rc) {
        /* Redundant:
        for(Specie specie : SpeciesRegistry.instance.getAll())
            new BeeBreedingRecipeBuilder(specie, specie, specie)
                .save(rc);*/

        //breed(rc,"common", "forest", "tempered", true);

        for(Specie specie : SpeciesRegistry.instance.getAll()) {
            if(specie.produce == null)
                continue;
            produce(rc, specie.getName(), specie.produce.common(), specie.produce.commonCountUnsatisfied(), specie.produce.commonCountSatisfied(),
                specie.produce.rare(), specie.produce.rareChanceUnsatisfied(), specie.produce.rareChanceSatisfied());

            for(Pair<String, String> pair : specie.breeding) {
                breed(rc, Specie.getByName(pair.getFirst()), Specie.getByName(pair.getSecond()), specie);
            }
        }

        //produce(rc, "common", Items.HONEYCOMB, 9, 13, null, 0, 0);
        //produce(rc, "forest", Items.HONEYCOMB, 9, 13, null, 0, 0);
        //produce(rc, "tempered", Items.HONEYCOMB, 7, 14, null, 0, 0);
    }

    private void breed(Consumer<FinishedRecipe> recipeConsumer, Specie drone, Specie princess, Specie offspring) {
        new BeeBreedingRecipeBuilder(drone, princess, offspring)
                .save(recipeConsumer);
    }

    private void produce(Consumer<FinishedRecipe> recipeConsumer, String specie, Item common, int commonCountUnsatisfied, int commonCountSatisfied, Item rare,
                          double rareChanceUnsatisfied, double rareChanceSatisfied) {
        new BeeProduceRecipeBuilder(Specie.getByName(specie), new ItemStack(common, commonCountUnsatisfied), rare, rareChanceUnsatisfied,
                new ItemStack(common, commonCountSatisfied), rare, rareChanceSatisfied)
                .save(recipeConsumer);
    }
}
