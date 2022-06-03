package github.mrh0.beekeeping.datagen;

import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.SpeciesRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
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
    }

    private void breed(Consumer<FinishedRecipe> recipeConsumer, String drone, String princess, String offspring, boolean mirror) {
        new BeeBreedingRecipeBuilder(Specie.getByName(drone), Specie.getByName(princess), Specie.getByName(offspring))
                .save(recipeConsumer);
        if(mirror)
            new BeeBreedingRecipeBuilder(Specie.getByName(princess), Specie.getByName(drone), Specie.getByName(offspring))
                    .save(recipeConsumer);
    }
}
