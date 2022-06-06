package github.mrh0.beekeeping.datagen;

import com.google.gson.JsonObject;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.recipe.BeeBreedingRecipe;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BeeProduceRecipeBuilder implements RecipeBuilder {
    private final Specie specie;
    private final ItemStack commonProduceUnsatisfied;
    private final Item rareProduceUnsatisfied;
    private final double rareChanceUnsatisfied;

    private final ItemStack commonProduceSatisfied;
    private final Item rareProduceSatisfied;
    private final double rareChanceSatisfied;

    public BeeProduceRecipeBuilder(Specie specie,
           ItemStack commonProduceUnsatisfied, Item rareProduceUnsatisfied, double rareChanceUnsatisfied,
           ItemStack commonProduceSatisfied, Item rareProduceSatisfied, double rareChanceSatisfied) {
        this.specie = specie;
        this.commonProduceUnsatisfied = commonProduceUnsatisfied;
        this.rareProduceUnsatisfied = rareProduceUnsatisfied;
        this.rareChanceUnsatisfied = rareChanceUnsatisfied;

        this.commonProduceSatisfied = commonProduceSatisfied;
        this.rareProduceSatisfied = rareProduceSatisfied;
        this.rareChanceSatisfied = rareChanceSatisfied;
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
        return ItemStack.EMPTY.getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation recipeId) {
        recipeConsumer.accept(new BeeProduceRecipeBuilder.Result(recipeId, specie,
                commonProduceUnsatisfied, rareProduceUnsatisfied, rareChanceUnsatisfied,
                commonProduceSatisfied, rareProduceSatisfied, rareChanceSatisfied));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Specie specie;
        private final ItemStack commonProduceUnsatisfied;
        private final Item rareProduceUnsatisfied;
        private final double rareChanceUnsatisfied;

        private final ItemStack commonProduceSatisfied;
        private final Item rareProduceSatisfied;
        private final double rareChanceSatisfied;

        public Result(ResourceLocation id, Specie specie,
                      ItemStack commonProduceUnsatisfied, Item rareProduceUnsatisfied, double rareChanceUnsatisfied,
                      ItemStack commonProduceSatisfied, Item rareProduceSatisfied, double rareChanceSatisfied) {
            this.id = id;
            this.specie = specie;
            this.commonProduceUnsatisfied = commonProduceUnsatisfied;
            this.rareProduceUnsatisfied = rareProduceUnsatisfied;
            this.rareChanceUnsatisfied = rareChanceUnsatisfied;

            this.commonProduceSatisfied = commonProduceSatisfied;
            this.rareProduceSatisfied = rareProduceSatisfied;
            this.rareChanceSatisfied = rareChanceSatisfied;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("specie", specie.getName());
            JsonObject produce = new JsonObject();
            json.add("produce", produce);

            produce.add("unsatisfied", makeProduce(commonProduceUnsatisfied, rareProduceUnsatisfied, rareChanceUnsatisfied));
            produce.add("satisfied", makeProduce(commonProduceSatisfied, rareProduceSatisfied, rareChanceSatisfied));
        }

        private JsonObject makeProduce(ItemStack commonProduce, Item rareProduce, double rareChance) {
            JsonObject obj = new JsonObject();
            JsonObject common = new JsonObject();
            JsonObject rare = new JsonObject();
            obj.add("common", common);
            obj.add("rare", rare);

            common.addProperty("item", commonProduce.getItem().getRegistryName().toString());
            common.addProperty("count", commonProduce.getCount());

            rare.addProperty("item", rareProduce.getRegistryName().toString());
            rare.addProperty("chance", rareChance);
            return obj;
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(Beekeeping.MODID, "bee_produce/" + specie.getName());
        }

        @Override
        public RecipeSerializer<?> getType() {
            return BeeProduceRecipe.Serializer.INSTANCE;
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