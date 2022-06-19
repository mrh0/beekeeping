package github.mrh0.beekeeping.recipe;

import com.google.gson.JsonObject;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.item.BeeItem;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class BeeBreedingRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final Specie drone, princess, offspring;
    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack output;

    public BeeBreedingRecipe(ResourceLocation id, Specie drone, Specie princess, Specie offspring) {
        this.id = id;
        this.drone = drone;
        this.princess = princess;
        this.offspring = offspring;
        this.recipeItems = NonNullList.create();//NonNullList.of(Ingredient.of(drone.droneItem), Ingredient.of(princess.princessItem));
        recipeItems.add(Ingredient.of(drone.droneItem));
        recipeItems.add(Ingredient.of(princess.princessItem));

        this.output = new ItemStack(offspring.queenItem);
    }

    public Specie getOffspring() {
        return this.offspring;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return BeeItem.is(container.getItem(0), drone) && BeeItem.is(container.getItem(1), princess);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer container) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<BeeBreedingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<BeeBreedingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Beekeeping.MODID,"bee_breeding");

        @Override
        public BeeBreedingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Specie droneSpecie = Specie.getByName(GsonHelper.getAsString(json, "drone"));
            Specie princessSpecie = Specie.getByName(GsonHelper.getAsString(json, "princess"));
            Specie offspringSpecie = Specie.getByName(GsonHelper.getAsString(json, "offspring"));

            return new BeeBreedingRecipe(id, droneSpecie, princessSpecie, offspringSpecie);
        }

        @Override
        public BeeBreedingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Specie droneSpecie = Specie.getByName(buf.readUtf());
            Specie princessSpecie = Specie.getByName(buf.readUtf());
            Specie offspringSpecie = Specie.getByName(buf.readUtf());
            return new BeeBreedingRecipe(id, droneSpecie, princessSpecie, offspringSpecie);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BeeBreedingRecipe recipe) {
            buf.writeUtf(recipe.drone.getName());
            buf.writeUtf(recipe.princess.getName());
            buf.writeUtf(recipe.offspring.getName());
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
            return Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked")
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}