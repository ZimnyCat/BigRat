package bleach.hack.gui.clickgui.modulewindow;

import bleach.hack.gui.window.Window;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.ClickGui;
import bleach.hack.utils.ColourThingy;
import bleach.hack.utils.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Triple;

public abstract class ClickGuiWindow extends Window {

    protected MinecraftClient mc;

    public int mouseX;
    public int mouseY;

    public int keyDown = -1;
    public boolean lmDown = false;
    public boolean rmDown = false;
    public boolean lmHeld = false;
    public int mwScroll = 0;

    public ClickGuiWindow(int x1, int y1, int x2, int y2, String title, ItemStack icon) {
        super(x1, y1, x2, y2, title, icon);
        mc = MinecraftClient.getInstance();
    }

    public boolean shouldClose(int mX, int mY) {
        return false;
    }

    protected void drawBar(MatrixStack matrix, int mX, int mY, TextRenderer textRend) {
        /* background and title bar */
        fillGrey(matrix, x1, y1, x2, y2);
        //fillGradient(matrix, x1 + 1, y1 + 1, x2 - 2, y1 + 12, 0xff0000ff, 0xff4080ff);
    }

    public void fillGrey(MatrixStack matrix, int x1, int y1, int x2, int y2) {
        DrawableHelper.fill(matrix, x1, y1, x1 + 1, y2, ColourThingy.guiColour());
        DrawableHelper.fill(matrix, x1, y2, x2 + 1, y2 - 1, ColourThingy.guiColour());
        DrawableHelper.fill(matrix, x2, y1, x1, y1 + 1, ColourThingy.guiColour());
        DrawableHelper.fill(matrix, x2, y2, x2 + 1, y1, ColourThingy.guiColour());
    }

    public boolean mouseOver(int minX, int minY, int maxX, int maxY) {
        return mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY < maxY;
    }

    public Triple<Integer, Integer, String> getTooltip() {
        return null;
    }

    public void updateKeys(int mouseX, int mouseY, int keyDown, boolean lmDown, boolean rmDown, boolean lmHeld, int mwScroll) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.keyDown = keyDown;
        this.lmDown = lmDown;
        this.rmDown = rmDown;
        this.lmHeld = lmHeld;
        this.mwScroll = mwScroll;
    }
}
