package github.mrh0.beekeeping.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder <T extends Item> {
    T item;
    public final static List<Pair<RecipeBuilder, ItemLike>> recipes = new ArrayList<>();

    public ItemBuilder(T item) {
        this.item = item;
    }

    public ItemBuilder<T> shapeless(int count, ItemLike condition, Ingredient...ingredients) {
        var builder = new ShapelessRecipeBuilder(item, count);
        for(Ingredient ingredient : ingredients) {
            builder.requires(ingredient);
        }
        recipes.add(new Pair<>(builder, condition));
        return this;
    }

    public T build() {
        return item;
    }
}
