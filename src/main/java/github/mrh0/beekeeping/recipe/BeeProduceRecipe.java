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

public class BeeProduceRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final Specie specie;
    private final ItemStack commonProduce, rareProduce;

    private final NonNullList<Ingredient> recipeItems;

    public BeeProduceRecipe(ResourceLocation id, Specie specie, ItemStack commonProduce, ItemStack rareProduce) {
        this.id = id;
        this.specie = specie;
        this.commonProduce = commonProduce;
        this.rareProduce = rareProduce;
        this.recipeItems = NonNullList.of(
                Ingredient.of(specie.queenItem)
        );
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

            ItemStack commonProduce = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(produce, "common"));
            ItemStack rareProduce = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(produce, "rare"));

            return new BeeProduceRecipe(id, specie, commonProduce, rareProduce);
        }

        @Override
        public BeeProduceRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Specie specie = Specie.getByName(buf.readUtf());
            ItemStack commonProduce = buf.readItem();
            ItemStack rareProduce = buf.readItem();
            return new BeeProduceRecipe(id, specie, commonProduce, rareProduce);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BeeProduceRecipe recipe) {
            buf.writeUtf(recipe.specie.getName());
            buf.writeItemStack(recipe.commonProduce, false);
            buf.writeItemStack(recipe.rareProduce, false);
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