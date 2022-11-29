package github.mrh0.beekeeping.item;

import github.mrh0.beekeeping.group.ItemGroup;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder <T extends Item> {
    T item;
    public final static List<RecipeBuilder> recipes = new ArrayList<>();

    public ItemBuilder(T item) {
        this.item = item;
    }

    public ItemBuilder<T> shapeless(int count, Ingredient...ingredients) {
        var builder = new ShapelessRecipeBuilder(item, count);
        for(Ingredient ingredient : ingredients) {
            builder.requires(ingredient);
        }
        recipes.add(builder);
        return this;
    }

    public T build() {
        return item;
    }
}
