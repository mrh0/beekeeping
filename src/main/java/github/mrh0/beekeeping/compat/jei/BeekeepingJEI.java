package github.mrh0.beekeeping.compat.jei;

import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.recipe.BeeBreedingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import mezz.jei.api.recipe.RecipeType;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class BeekeepingJEI implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Beekeeping.MODID, "jei_plugin");
    }

    private RecipeType<BeeBreedingRecipe> beeBreedingType = new RecipeType<>(BeeBreedingRecipeCategory.UID, BeeBreedingRecipe.class);

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                BeeBreedingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<BeeBreedingRecipe> recipes = rm.getAllRecipesFor(BeeBreedingRecipe.Type.INSTANCE);
        registration.addRecipes(beeBreedingType, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Index.APIARY_BLOCK.get()), beeBreedingType);
    }
}