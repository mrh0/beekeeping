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
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AnalyzerScreen extends BeeScreen<AnalyzerMenu, AnalyzerBlockEntity> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Beekeeping.MODID, "textures/gui/analyzer.png");

    int lx = 14, ly = 45;
    public AnalyzerScreen(AnalyzerMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        imageHeight = 211;
        inventoryLabelY = 121;
    }

    private Bounds getListBounds(int index) {
        return new Bounds(lx, ly + 14*index - 2, imageWidth - lx, 12);
    }

    private void drawListItem(PoseStack poseStack, Component text, int index, int image) {
        drawText(poseStack, text, lx + 12, ly + 14*index, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(poseStack, lx + getXOffset(), ly + 14*index + getYOffset(), imageWidth, 8*image, 8, 8);
    }

    private Bounds lifetimeBounds = getListBounds(0);
    private Bounds weatherBounds = getListBounds(1);
    private Bounds temperatureBounds = getListBounds(2);
    private Bounds lightBounds = getListBounds(3);
    private Bounds produceBounds = getListBounds(4);

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
        int rareProduceBonus = (int)((rareProduceGene.getChance()-1d)*100d);

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
                getSpecie().isHasRareProduce() ? rareProduceGene.getComponent().append(" ").append((rareProduceBonus >= 0 ? "+" : "") + rareProduceBonus + "%")
                 : new TextComponent("").append(rareProduceGene.getComponent().withStyle(ChatFormatting.STRIKETHROUGH))
                        .append(" ").append(new TranslatableComponent("text.beekeeping.none").withStyle(ChatFormatting.RED)),
                line++, 5);
    }

    private static List<Component> lifetimeDescription = List.of(new TranslatableComponent("tooltip.beekeeping.gene.lifetime"));
    private static List<Component> weatherDescription = List.of(new TranslatableComponent("tooltip.beekeeping.gene.weather"));
    private static List<Component> temperatureDescription = List.of(new TranslatableComponent("tooltip.beekeeping.gene.temperature"));
    private static List<Component> lightDescription = List.of(new TranslatableComponent("tooltip.beekeeping.gene.light"));
    private static List<Component> produceDescription = List.of(new TranslatableComponent("tooltip.beekeeping.gene.produce"));
    @Override
    protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
        super.renderTooltip(poseStack, mouseX, mouseY);
        if(lifetimeBounds.in(mouseX, mouseY))
            renderComponentTooltip(poseStack, lifetimeDescription, mouseX, mouseY);
        else if(weatherBounds.in(mouseX, mouseY))
            renderComponentTooltip(poseStack, weatherDescription, mouseX, mouseY);
        else if(temperatureBounds.in(mouseX, mouseY))
            renderComponentTooltip(poseStack, temperatureDescription, mouseX, mouseY);
        else if(lightBounds.in(mouseX, mouseY))
            renderComponentTooltip(poseStack, lightDescription, mouseX, mouseY);
        else if(produceBounds.in(mouseX, mouseY))
            renderComponentTooltip(poseStack, produceDescription, mouseX, mouseY);
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
        return BeeItem.speciesOf(getAnalyzed());
    }
}