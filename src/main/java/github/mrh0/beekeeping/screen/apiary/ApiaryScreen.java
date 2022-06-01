package github.mrh0.beekeeping.screen.apiary;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.blocks.apiary.ApiaryBlockEntity;
import github.mrh0.beekeeping.screen.BeeScreen;
import github.mrh0.beekeeping.screen.analyzer.AnalyzerMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class ApiaryScreen extends BeeScreen<ApiaryMenu, ApiaryBlockEntity> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Beekeeping.MODID, "textures/gui/apiary.png");

    public ApiaryScreen(ApiaryMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        imageHeight = 176;
        inventoryLabelY = 86;
    }

    private Bounds toggle = new Bounds(50, 67, 20, 8);
    private boolean toggleState = false;
    private Bounds warning = new Bounds(66, 36, 8, 8, 4, 4);

    @Override
    protected void renderBg(PoseStack poseStack, float partial, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = getXOffset();
        int y = getYOffset();

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
        drawWarning(poseStack);
        drawToggle(poseStack, (toggle.in(mouseX, mouseY) ? 2 : 0) + (toggleState ? 1 : 0));
    }

    private void drawWarning(PoseStack poseStack) {
        this.blit(poseStack, warning.getX(), warning.getY(), imageWidth, 32, warning.w, warning.h);
    }

    private void drawToggle(PoseStack poseStack, int i) {
        this.blit(poseStack, toggle.getX(), toggle.getY(), imageWidth, i*toggle.h, toggle.w, toggle.h);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    public void onLeftClicked(int x, int y) {
        if(toggle.in(x, y))
            toggleState = !toggleState;
    }

    private static List<Component> toggleOnTip = new ArrayList<>();
    private static List<Component> toggleOffTip = new ArrayList<>();

    private static List<Component> warningTip = new ArrayList<>();

    static {
        toggleOnTip.add(new TranslatableComponent("tooltip.beekeeping.apiary.continuous"));
        toggleOnTip.add(new TextComponent("On").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        toggleOffTip.add(new TranslatableComponent("tooltip.beekeeping.apiary.continuous"));
        toggleOffTip.add(new TextComponent("Off").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));

        warningTip.add(new TextComponent("Test"));
    }

    @Override
    protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
        super.renderTooltip(poseStack, mouseX, mouseY);
        if(toggle.in(mouseX, mouseY)) {
            renderComponentTooltip(poseStack, toggleState ? toggleOnTip : toggleOffTip, mouseX, mouseY);
        }
        if(warning.in(mouseX, mouseY)) {
            renderComponentTooltip(poseStack, warningTip, mouseX, mouseY);
        }
    }
}