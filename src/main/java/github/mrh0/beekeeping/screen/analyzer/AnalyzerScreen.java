package github.mrh0.beekeeping.screen.analyzer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.genes.*;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlockEntity;
import github.mrh0.beekeeping.screen.BeeScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
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
        return new Bounds(lx, ly + 14*index, 8, 8);
    }

    private void drawListItem(PoseStack poseStack, Component text, int index, int image) {
        drawText(poseStack, text, lx + 12, ly + 14*index, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(poseStack, lx + getXOffset(), ly + 14*index + getYOffset(), imageWidth, 8*image, 8, 8);
    }

    int lx = 14, ly = 45;
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

        LightToleranceGene lightTolerance = LightToleranceGene.of(LightToleranceGene.get(getAnalyzed().getTag()));
        int lightToleranceImage = lightTolerance == LightToleranceGene.ANY ? 2 : (getSpecie().isNocturnal ? 1 : 0);

        int temperatureImage = getSpecie().preferredTemperature.ordinal();

        RareProduceGene rareProduceGene = RareProduceGene.of(RareProduceGene.get(getAnalyzed().getTag()));

        int line = 0;
        drawListItem(poseStack, LifetimeGene.of(LifetimeGene.get(getAnalyzed().getTag())).getComponent(), line++, 4);
        drawListItem(poseStack, WeatherToleranceGene.of(WeatherToleranceGene.get(getAnalyzed().getTag())).getComponent(), line++, 3);
        drawListItem(poseStack, TemperatureToleranceGene.of(TemperatureToleranceGene.get(getAnalyzed().getTag())).getComponent()
                .append(" ").append(getSpecie().preferredTemperature.getComponent()), line++, 6 + temperatureImage);
        drawListItem(poseStack,
                getSpecie().isNocturnal ?
                lightTolerance.getComponent().append(" ").append(new TranslatableComponent("text.beekeeping.nocturnal").withStyle(ChatFormatting.DARK_BLUE)) : lightTolerance.getComponent(),
                line++, lightToleranceImage);
        drawListItem(poseStack,
                getSpecie().isHasRareProduce() ? rareProduceGene.getComponent()
                 : new TextComponent("").append(rareProduceGene.getComponent().withStyle(ChatFormatting.STRIKETHROUGH))
                        .append(" ").append(new TranslatableComponent("text.beekeeping.none").withStyle(ChatFormatting.RED)),
                line++, 5);
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