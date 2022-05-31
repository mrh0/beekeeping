package github.mrh0.beekeeping.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class BeeScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    public BeeScreen(T menu, Inventory inv, Component text) {
        super(menu, inv, text);
    }

    public int getXOffset() {
        return (width - imageWidth) / 2;
    }

    public int getYOffset() {
        return (height - imageHeight) / 2;
    }

    public class Bounds {
        public final int x, y, w, h, mw, mh;
        public Bounds(int x, int y, int w, int h) {
            this(x, y, w, h,0, 0);
        }

        public Bounds(int x, int y, int w, int h, int mw, int mh) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.mw = mw;
            this.mh = mh;
        }

        public boolean in(int mx, int my) {
            int xo = getXOffset();
            int yo = getYOffset();
            return mx >= x+xo-mw && my >= y+yo-mh && mx < x+w+xo+mw && my < y+h+yo+mh;
        }

        public int getX() {
            return x + getXOffset();
        }

        public int getY() {
            return y + getYOffset();
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        switch(btn) {
            case 0:
                onLeftClicked((int)x, (int)y);
                break;
            case 1:
                onRightClicked((int)x, (int)y);
                break;
        }
        return super.mouseClicked(x, y, btn);
    }

    public void onLeftClicked(int x, int y) {}
    public void onRightClicked(int x, int y) {}

    /*public void draw(PoseStack poseStack) {
        blit(poseStack);
    }*/
}
