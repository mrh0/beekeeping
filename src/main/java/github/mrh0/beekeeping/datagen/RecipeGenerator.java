package github.mrh0.beekeeping.datagen;

import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
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
        breed(rc,"common", "forest", "tempered", true);
        produce(rc, "common", Items.STICK, 1, 2, Items.STONE, 0.5, 0.75);
    }

    private void breed(Consumer<FinishedRecipe> recipeConsumer, String drone, String princess, String offspring, boolean mirror) {
        new BeeBreedingRecipeBuilder(Specie.getByName(drone), Specie.getByName(princess), Specie.getByName(offspring))
                .save(recipeConsumer);
        if(mirror)
            new BeeBreedingRecipeBuilder(Specie.getByName(princess), Specie.getByName(drone), Specie.getByName(offspring))
                    .save(recipeConsumer);
    }

    private void produce(Consumer<FinishedRecipe> recipeConsumer, String specie, Item common, int commonCountUnsatisfied, int commonCountSatisfied, Item rare,
                          double rareChanceUnsatisfied, double rareChanceSatisfied) {
        new BeeProduceRecipeBuilder(Specie.getByName(specie), new ItemStack(common, commonCountUnsatisfied), rare, rareChanceUnsatisfied,
                new ItemStack(common, commonCountSatisfied), rare, rareChanceSatisfied)
                .save(recipeConsumer);
    }
}
