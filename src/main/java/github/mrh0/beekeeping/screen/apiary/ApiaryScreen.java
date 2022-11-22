package github.mrh0.beekeeping.screen.apiary;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.Satisfaction;
import github.mrh0.beekeeping.bee.Specie;
import github.mrh0.beekeeping.bee.item.BeeItem;
import github.mrh0.beekeeping.blocks.apiary.ApiaryBlockEntity;
import github.mrh0.beekeeping.network.TogglePacket;
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
    private boolean getToggleState() {
        return getBlockEntity().continuous;
    }
    private Bounds satisfaction = new Bounds(66, 36, 8, 8, 4, 4);
    private Bounds health = new Bounds(76, 37, 4, 26);
    private Bounds breedProgress = new Bounds(15, 43, 32, 15);

    @Override
    protected void renderBg(PoseStack poseStack, float partial, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = getXOffset();
        int y = getYOffset();

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
        drawToggle(poseStack, (toggle.in(mouseX, mouseY) ? 2 : 0) + (getToggleState() ? 1 : 0));

        if(getQueen() != null && !getQueen().isEmpty() && getQueen().getTag() != null) {
            drawImagePartBottomUp(poseStack, health, imageWidth, 87, BeeItem.getHealthOf(getQueen()));

            drawSatisfaction(poseStack);
        }

        drawBreedProgress(poseStack, (double)getMenu().data.get(0)/(double)getBlockEntity().BREED_TIME);
    }

    private void drawSatisfaction(PoseStack poseStack) {
        Satisfaction weatherSatisfaction = Satisfaction.of(getMenu().data.get(2));
        Satisfaction temperatureSatisfaction = Satisfaction.of(getMenu().data.get(3));
        Satisfaction lightSatisfaction = Satisfaction.of(getMenu().data.get(4));

        Satisfaction s = Satisfaction.calc(weatherSatisfaction, temperatureSatisfaction, lightSatisfaction);
        this.blit(poseStack, satisfaction.getX(), satisfaction.getY(), imageWidth, 32 + s.ordinal()*satisfaction.h, satisfaction.w, satisfaction.h);
    }

    private void drawToggle(PoseStack poseStack, int i) {
        this.blit(poseStack, toggle.getX(), toggle.getY(), imageWidth, i*toggle.h, toggle.w, toggle.h);
    }

    private void drawBreedProgress(PoseStack poseStack, double f) {
        drawImagePartHorizontal(poseStack, breedProgress, imageWidth, 56, f);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    public void onLeftClicked(int x, int y) {
        if(toggle.in(x, y)) {
            //getBlockEntity().continuous = !getToggleState();
            TogglePacket.send(getBlockPos(), getLevel(), 0, !getToggleState());
        }
    }

    private static List<Component> toggleOnTip = new ArrayList<>();
    private static List<Component> toggleOffTip = new ArrayList<>();

    static {
        toggleOnTip.add(new TranslatableComponent("tooltip.beekeeping.apiary.continuous"));
        toggleOnTip.add(new TextComponent("On").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        toggleOffTip.add(new TranslatableComponent("tooltip.beekeeping.apiary.continuous"));
        toggleOffTip.add(new TextComponent("Off").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
    }

    private static MutableComponent checkExcCross(Satisfaction satisfaction) {
        return switch (satisfaction) {
            case SATISFIED -> new TextComponent("✔ ").withStyle(ChatFormatting.GREEN);
            case UNSATISFIED -> new TextComponent(" ! ").withStyle(ChatFormatting.YELLOW);
            default -> new TextComponent("✘ ").withStyle(ChatFormatting.RED);
        };
    }

    private List<Component> buildSatisfactionTooltip(ItemStack queen) {
        List<Component> tip = new ArrayList<>();

        Satisfaction weatherSatisfaction = Satisfaction.of(getMenu().data.get(2));
        Satisfaction temperatureSatisfaction = Satisfaction.of(getMenu().data.get(3));
        Satisfaction lightSatisfaction = Satisfaction.of(getMenu().data.get(4));

        Satisfaction satisfaction = Satisfaction.calc(weatherSatisfaction, temperatureSatisfaction, lightSatisfaction);
        tip.add(checkExcCross(satisfaction).append(satisfaction.component).withStyle(ChatFormatting.BOLD));
        tip.add(checkExcCross(weatherSatisfaction).append(new TranslatableComponent("tooltip.beekeeping.apiary.weather")));
        tip.add(checkExcCross(temperatureSatisfaction).append(new TranslatableComponent("tooltip.beekeeping.apiary.temperature")));
        tip.add(checkExcCross(lightSatisfaction).append(new TranslatableComponent("tooltip.beekeeping.apiary.light")));

        return tip;
    }

    @Override
    protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
        if(toggle.in(mouseX, mouseY)) {
            renderComponentTooltip(poseStack, getToggleState() ? toggleOnTip : toggleOffTip, mouseX, mouseY);
        }
        else if(satisfaction.in(mouseX, mouseY) && !getQueen().isEmpty()) {
            renderComponentTooltip(poseStack, buildSatisfactionTooltip(getQueen()), mouseX, mouseY);
        }
        else
            super.renderTooltip(poseStack, mouseX, mouseY);
    }

    public ItemStack getQueen() {
        return getBlockEntity().getQueen();
    }
}