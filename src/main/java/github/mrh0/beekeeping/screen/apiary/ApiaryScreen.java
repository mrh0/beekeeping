package github.mrh0.beekeeping.screen.apiary;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.blocks.apiary.ApiaryBlockEntity;
import github.mrh0.beekeeping.screen.BeeScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
    private Bounds satisfaction = new Bounds(66, 36, 8, 8, 4, 4);
    private Bounds health = new Bounds(76, 37, 4, 26);

    @Override
    protected void renderBg(PoseStack poseStack, float partial, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = getXOffset();
        int y = getYOffset();

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
        drawToggle(poseStack, (toggle.in(mouseX, mouseY) ? 2 : 0) + (toggleState ? 1 : 0));

        if(getQueen() != null && !getQueen().isEmpty()) {
            drawImagePartBottomUp(poseStack, health, imageWidth, 87, BeeItem.getHealthOf(getQueen()));

            drawSatisfaction(poseStack);
        }
    }

    private void drawSatisfaction(PoseStack poseStack) {
        Specie specie = BeeItem.of(getQueen());
        if(specie == null)
            return;

        Specie.Satisfaction biomeSatisfaction = specie.getBiomeSatisfaction(getQueen(), getLevel(), getBlockPos());

        Specie.Satisfaction s = Specie.Satisfaction.calc(biomeSatisfaction);
        this.blit(poseStack, satisfaction.getX(), satisfaction.getY(), imageWidth, 32 + s.index*satisfaction.h, satisfaction.w, satisfaction.h);
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

    static {
        toggleOnTip.add(new TranslatableComponent("tooltip.beekeeping.apiary.continuous"));
        toggleOnTip.add(new TextComponent("On").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        toggleOffTip.add(new TranslatableComponent("tooltip.beekeeping.apiary.continuous"));
        toggleOffTip.add(new TextComponent("Off").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
    }

    private static MutableComponent checkExcCross(Specie.Satisfaction satisfaction) {
        return switch (satisfaction) {
            case SATISFIED -> new TextComponent("✔ ").withStyle(ChatFormatting.GREEN);
            case UNSATISFIED -> new TextComponent("! ").withStyle(ChatFormatting.YELLOW);
            default -> new TextComponent("✘ ").withStyle(ChatFormatting.RED);
        };
    }

    private static List<Component> buildSatisfactionTooltip(ItemStack queen, Level level, BlockPos pos) {
        List<Component> tip = new ArrayList<>();
        Specie specie = BeeItem.of(queen);
        if(specie == null)
            return tip;

        Specie.Satisfaction biomeSatisfaction = specie.getBiomeSatisfaction(queen, level, pos);

        Specie.Satisfaction satisfaction = Specie.Satisfaction.calc(biomeSatisfaction);
        tip.add(checkExcCross(satisfaction).append(satisfaction.component).withStyle(ChatFormatting.BOLD));

        tip.add(checkExcCross(biomeSatisfaction).append(new TranslatableComponent("tooltip.beekeeping.apiary.biome")));

        return tip;
    }

    @Override
    protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
        super.renderTooltip(poseStack, mouseX, mouseY);
        if(toggle.in(mouseX, mouseY)) {
            renderComponentTooltip(poseStack, toggleState ? toggleOnTip : toggleOffTip, mouseX, mouseY);
        }
        if(satisfaction.in(mouseX, mouseY)) {
            renderComponentTooltip(poseStack, buildSatisfactionTooltip(getQueen(), getLevel(), getBlockPos()), mouseX, mouseY);
        }
    }

    public ItemStack getQueen() {
        return getBlockEntity().getQueen();
    }
}