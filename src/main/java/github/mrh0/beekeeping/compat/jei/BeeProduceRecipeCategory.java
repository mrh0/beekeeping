package github.mrh0.beekeeping.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.Index;
import github.mrh0.beekeeping.network.ClientMinecraftWrapper;
import github.mrh0.beekeeping.recipe.BeeProduceRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class BeeProduceRecipeCategory implements IRecipeCategory<BeeProduceRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(Beekeeping.MODID, "bee_produce");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(Beekeeping.MODID, "textures/gui/bee_produce.png");

    private final IDrawable background;
    private final IDrawable icon;

    public BeeProduceRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 118, 53);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Index.APIARY_BLOCK.get()));
    }

    @Override
    public RecipeType<BeeProduceRecipe> getRecipeType() {
        return new RecipeType<>(UID, BeeProduceRecipe.class);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("title.beekeeping.recipe.bee_produce");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull BeeProduceRecipe recipe, @Nonnull IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 7, 8).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 7, 29).addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 44, 8).addItemStack(recipe.getCommonProduce(true));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 44, 29).addItemStack(recipe.getCommonProduce(false));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 8).addItemStack(recipe.getRareProduce(true));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 29).addItemStack(recipe.getRareProduce(false));
    }

    @Override
    public void draw(BeeProduceRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);

        if(recipe.getRareChance(true) > 0 && recipe.getRareChance(true) < 1)
            ClientMinecraftWrapper.get().font.draw(stack, (int)(recipe.getRareChance(true)*100) + "%", 100, 12, 4210752);
        if(recipe.getRareChance(false) > 0 && recipe.getRareChance(false) < 1)
            ClientMinecraftWrapper.get().font.draw(stack, (int)(recipe.getRareChance(false)*100) + "%", 100, 34, 4210752);
    }
}