package github.mrh0.beekeeping.screen.analyzer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.blocks.analyzer.AnalyzerBlockEntity;
import github.mrh0.beekeeping.screen.BeeScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AnalyzerScreen extends BeeScreen<AnalyzerMenu, AnalyzerBlockEntity> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Beekeeping.MODID, "textures/gui/analyzer.png");

    public AnalyzerScreen(AnalyzerMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        imageHeight = 211;
        inventoryLabelY = 121;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partial, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = getXOffset();
        int y = getYOffset();

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        drawText(poseStack, new TranslatableComponent("test text"), 39, 24, 2f);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }
}