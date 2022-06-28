package github.mrh0.beekeeping.screen.analyzer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.genes.LifetimeGene;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlockEntity;
import github.mrh0.beekeeping.screen.BeeScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class AnalyzerScreen extends BeeScreen<AnalyzerMenu, AnalyzerBlockEntity> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Beekeeping.MODID, "textures/gui/analyzer.png");

    public AnalyzerScreen(AnalyzerMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        imageHeight = 211;
        inventoryLabelY = 121;
    }

    private Bounds getListBounds(int index) {
        return new Bounds(lx, ly, 8, 8);
    }

    private void drawListItem(PoseStack poseStack, Bounds bounds, Component text, int index, int image) {
        drawText(poseStack, text, lx + 12, 44 + 14*index, 1f);
        blit(poseStack, bounds.getX(), bounds.getY(), imageWidth, bounds.h*image, bounds.w, bounds.h);
    }

    int lx = 14, ly = 44;
    private Bounds lifetimeBounds = getListBounds(0);

    @Override
    protected void renderBg(PoseStack poseStack, float partial, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = getXOffset();
        int y = getYOffset();

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        if(getAnalyzed() != null && getSpecie() != null) {
            drawText(poseStack, new TranslatableComponent("item.beekeeping.species." + getSpecie().getName()), 39, 24, 1.75f, getSpecie().getColor());
        }
        else
            drawText(poseStack, new TranslatableComponent("title.beekeeping.analyzer.insert"), 39, 24, 1.75f);
        /*float temp = getLevel().getBiomeManager().getBiome(getBlockPos()).value().getBaseTemperature();
        drawText(poseStack, new TranslatableComponent("title.beekeeping.analyzer.temp").append(": ").append(temp+""), 14, 44, 1f);
        float rain = getLevel().getBiomeManager().getBiome(getBlockPos()).value().getDownfall();
        drawText(poseStack, new TranslatableComponent("title.beekeeping.analyzer.rain").append(": ").append(rain+""), 14, 60, 1f);*/

        if(getSpecie() == null)
            return;


        drawListItem(poseStack, lifetimeBounds, LifetimeGene.of(LifetimeGene.get(getAnalyzed().getTag())).getComponent(), 0, 4);
        drawListItem(poseStack, lifetimeBounds, LifetimeGene.of(LifetimeGene.get(getAnalyzed().getTag())).getComponent(), 1, 3);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    public ItemStack getAnalyzed() {
        return getBlockEntity().getAnalyzed();
    }

    public Specie getSpecie() {
        return BeeItem.of(getAnalyzed());
    }
}