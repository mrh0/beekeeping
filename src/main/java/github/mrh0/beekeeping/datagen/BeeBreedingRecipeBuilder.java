package github.mrh0.beekeeping.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.recipe.BeeBreedingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BeeBreedingRecipeBuilder implements RecipeBuilder {
    private final Specie drone;
    private final Specie princess;
    private final Specie offspring;

    public BeeBreedingRecipeBuilder(Specie drone, Specie princess, Specie offspring) {
        this.drone = drone;
        this.princess = princess;
        this.offspring = offspring;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, CriterionTriggerInstance trigger) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return offspring.queenItem;
    }

    @Override
    public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation recipeId) {
        recipeConsumer.accept(new BeeBreedingRecipeBuilder.Result(recipeId, drone, princess, offspring));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Specie drone;
        private final Specie princess;
        private final Specie offspring;

        public Result(ResourceLocation id, Specie drone, Specie princess, Specie offspring) {
            this.id = id;
            this.drone = drone;
            this.princess = princess;
            this.offspring = offspring;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("drone", drone.getName());
            json.addProperty("princess", princess.getName());
            json.addProperty("offspring", offspring.getName());
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(Beekeeping.MODID, "bee_breeding/" + drone.getName() + "_drone_with_" + princess.getName() + "_princess");
        }

        @Override
        public RecipeSerializer<?> getType() {
            return BeeBreedingRecipe.Serializer.INSTANCE;
        }

        @javax.annotation.Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @javax.annotation.Nullable
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}