package github.mrh0.beekeeping.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import github.mrh0.beekeeping.Beekeeping;
import github.mrh0.beekeeping.bee.genes.LifetimeGene;
import github.mrh0.beekeeping.biome.BiomeTemperature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Beekeeping.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientOverlay {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Beekeeping.MODID, "textures/gui/analyzer.png");
    static int lx = 16, ly = 16;
    static ItemStack thermometer = new ItemStack();

    @SubscribeEvent
    public static void renderOverlay(final RenderGameOverlayEvent.Pre event) {
        var stack = event.getMatrixStack();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        var player = Minecraft.getInstance().player;
        if(player == null)
            return;
        if(player.level == null)
            return;

        if(!player.getInventory().contains(thermometer))
            return;

        var temp = BiomeTemperature.of(player.level.getBiome(Minecraft.getInstance().player.getOnPos()).value().getBaseTemperature());

        //drawTextShadowed(stack, new TextComponent("Test"), 10, 10, 1f);
        int line = 0;
        drawListItem(stack, temp.getComponent(), line++, 6 + temp.ordinal());
    }

    public static void drawTextShadowed(PoseStack poseStack, Component text, int x, int y, float scale) {
        drawText(poseStack, text.plainCopy(), x + 1, y + 1, scale, 4210752);
        drawText(poseStack, text, x, y, scale, 16777215);
    }

    public static void drawText(PoseStack poseStack, Component text, int x, int y, float scale) {
        drawText(poseStack, text, x, y, scale, 4210752);
    }

    public static void drawText(PoseStack poseStack, Component text, int x, int y, float scale, int color) {
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        Minecraft.getInstance().font.draw(poseStack, text, (float)(x) / scale, (float)(y) / scale, color);
        poseStack.popPose();
    }

    private static void drawListItem(PoseStack poseStack, Component text, int index, int image) {
        drawTextShadowed(poseStack, text, lx + 12, ly + 14*index, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(poseStack, lx, ly + 14*index, 176, 8*image, 8, 8);
    }

    public static void blit(PoseStack p_93229_, int p_93230_, int p_93231_, int p_93232_, int p_93233_, int p_93234_, int p_93235_) {
        GuiComponent.blit(p_93229_, p_93230_, p_93231_, 0, (float)p_93232_, (float)p_93233_, p_93234_, p_93235_, 256, 256);
    }
}
