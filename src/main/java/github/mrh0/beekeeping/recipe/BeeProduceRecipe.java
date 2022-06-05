package github.mrh0.beekeeping.recipe;

import com.google.gson.JsonObject;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Util;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.item.BeeItem;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class BeeProduceRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final Specie specie;
    private final ItemStack commonProduceUnsatisfied;
    private final Item rareProduceUnsatisfied;
    private final double rareChanceUnsatisfied;

    private final ItemStack commonProduceSatisfied;
    private final Item rareProduceSatisfied;
    private final double rareChanceSatisfied;

    private final NonNullList<Ingredient> recipeItems;

    public BeeProduceRecipe(
            ResourceLocation id, Specie specie,
            ItemStack commonProduceSatisfied, Item rareProduceSatisfied, double rareChanceSatisfied,
            ItemStack commonProduceUnsatisfied, Item rareProduceUnsatisfied, double rareChanceUnsatisfied
    ) {
        this.id = id;
        this.specie = specie;
        this.commonProduceUnsatisfied = commonProduceUnsatisfied;
        this.rareProduceUnsatisfied = rareProduceUnsatisfied;
        this.rareChanceUnsatisfied = rareChanceUnsatisfied;

        this.commonProduceSatisfied = commonProduceSatisfied ;
        this.rareProduceSatisfied = rareProduceSatisfied ;
        this.rareChanceSatisfied = rareChanceSatisfied ;

        this.recipeItems = NonNullList.of(
                Ingredient.of(specie.queenItem)
        );
    }

    public Specie getSpecie() {
        return specie;
    }

    public ItemStack getCommonProduce(boolean satisfied) {
        return satisfied ? commonProduceSatisfied.copy() : commonProduceUnsatisfied.copy();
    }

    public Item getRareProduce(boolean satisfied) {
        return satisfied ? rareProduceSatisfied : rareProduceUnsatisfied;
    }

    public double getRareChance(boolean satisfied) {
        return satisfied ? rareChanceSatisfied : rareChanceUnsatisfied;
    }

    public ItemStack getRolledRareProduce(boolean satisfied) {
        return Util.rollChance(new ItemStack(getRareProduce(satisfied)), getRareChance(satisfied));
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return BeeItem.is(container.getItem(0), specie);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer container) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BeeProduceRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return BeeProduceRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<BeeProduceRecipe> {
        private Type() { }
        public static final BeeProduceRecipe.Type INSTANCE = new BeeProduceRecipe.Type();
    }

    public static class Serializer implements RecipeSerializer<BeeProduceRecipe> {
        public static final BeeProduceRecipe.Serializer INSTANCE = new BeeProduceRecipe.Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Beekeeping.MODID,"bee_produce");

        @Override
        public BeeProduceRecipe fromJson(ResourceLocation id, JsonObject json) {
            Specie specie = Specie.getByName(GsonHelper.getAsString(json, "specie"));

            JsonObject produce = GsonHelper.getAsJsonObject(json, "produce");
            JsonObject satisfied = GsonHelper.getAsJsonObject(produce, "satisfied");
            JsonObject unsatisfied = GsonHelper.getAsJsonObject(produce, "unsatisfied");

            ItemStack commonProduceUnsatisfied = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(produce, "common"));
            JsonObject rareUnsatisfied = GsonHelper.getAsJsonObject(produce, "rare");
            Item rareProduceUnsatisfied = ShapedRecipe.itemFromJson(rareUnsatisfied);
            double rareChanceUnsatisfied = GsonHelper.getAsDouble(rareUnsatisfied, "chance");

            ItemStack commonProduceSatisfied = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(produce, "common"));
            JsonObject rareSatisfied = GsonHelper.getAsJsonObject(produce, "rare");
            Item rareProduceSatisfied = ShapedRecipe.itemFromJson(rareUnsatisfied);
            double rareChanceSatisfied = GsonHelper.getAsDouble(rareUnsatisfied, "chance");

            return new BeeProduceRecipe(id, specie,
                    commonProduceSatisfied, rareProduceSatisfied, rareChanceSatisfied,
                    commonProduceUnsatisfied, rareProduceUnsatisfied, rareChanceUnsatisfied);
        }

        @Override
        public BeeProduceRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Specie specie = Specie.getByName(buf.readUtf());
            ItemStack commonProduceUnsatisfied = buf.readItem();
            ItemStack rareProduceUnsatisfied = buf.readItem();
            double rareChanceUnsatisfied = buf.readDouble();

            ItemStack commonProduceSatisfied = buf.readItem();
            ItemStack rareProduceSatisfied = buf.readItem();
            double rareChanceSatisfied = buf.readDouble();

            return new BeeProduceRecipe(id, specie,
                    commonProduceSatisfied, rareProduceSatisfied.getItem(), rareChanceSatisfied,
                    commonProduceUnsatisfied, rareProduceUnsatisfied.getItem(), rareChanceUnsatisfied);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BeeProduceRecipe recipe) {
            buf.writeUtf(recipe.specie.getName());
            buf.writeItemStack(recipe.commonProduceUnsatisfied, false);
            buf.writeItemStack(new ItemStack(recipe.rareProduceUnsatisfied), false);
            buf.writeDouble(recipe.rareChanceUnsatisfied);
            buf.writeItemStack(recipe.commonProduceSatisfied, false);
            buf.writeItemStack(new ItemStack(recipe.rareProduceSatisfied), false);
            buf.writeDouble(recipe.rareChanceSatisfied);
        }

        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        @Override
        public ResourceLocation getRegistryName() {
            return ID;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType() {
            return BeeProduceRecipe.Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked")
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}