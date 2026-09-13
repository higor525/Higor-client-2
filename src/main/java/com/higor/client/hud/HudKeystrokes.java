package com.higor.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class HudKeystrokes extends HudModule {

    public HudKeystrokes() {
        super("Keystrokes");
    }

    @Override
    public void onRender() {
        Minecraft mc = Minecraft.getMinecraft();

        GlStateManager.pushMatrix();
        GlStateManager.translate(getPosX(), getPosY(), 0);
        GlStateManager.scale(getScale(), getScale(), 1);

        int bg = getBgColor();
        int textColor = getTextColor();
        int box = 16;
        int gap = 2;

        // W
        drawKey(mc, "W", Keyboard.isKeyDown(Keyboard.KEY_W),
                0, 0, box, bg, textColor);

        // A
        drawKey(mc, "A", Keyboard.isKeyDown(Keyboard.KEY_A),
                -(box + gap), box + gap, box, bg, textColor);

        // S
        drawKey(mc, "S", Keyboard.isKeyDown(Keyboard.KEY_S),
                0, box + gap, box, bg, textColor);

        // D
        drawKey(mc, "D", Keyboard.isKeyDown(Keyboard.KEY_D),
                box + gap, box + gap, box, bg, textColor);

        // LMB
        boolean lmb = Mouse.isButtonDown(0);
        drawKeyWide(mc, "LMB", lmb, -(box + gap), (box + gap) * 2,
                box * 2 + gap, bg, textColor);

        // RMB
        boolean rmb = Mouse.isButtonDown(1);
        drawKeyWide(mc, "RMB", rmb, box / 2 + gap,
                (box + gap) * 2, box * 2 + gap, bg, textColor);

        GlStateManager.popMatrix();
    }

    private void drawKey(Minecraft mc, String label, boolean pressed,
                         int x, int y, int size, int bg, int textColor) {
        int color = pressed ? 0xFFFFFFFF : bg;
        int textCol = pressed ? 0xFF000000 : textColor;

        if (hasBackground() || pressed) {
            Gui.drawRect(x, y, x + size, y + size, color);
        }

        mc.fontRendererObj.drawString(label,
                x + (size - mc.fontRendererObj.getStringWidth(label)) / 2,
                y + (size - mc.fontRendererObj.FONT_HEIGHT) / 2,
                textCol);
    }

    private void drawKeyWide(Minecraft mc, String label, boolean pressed,
                             int x, int y, int width, int bg, int textColor) {
        int height = 16;
        int color = pressed ? 0xFFFFFFFF : bg;
        int textCol = pressed ? 0xFF000000 : textColor;

        if (hasBackground() || pressed) {
            Gui.drawRect(x, y, x + width, y + height, color);
        }

        mc.fontRendererObj.drawString(label,
                x + (width - mc.fontRendererObj.getStringWidth(label)) / 2,
                y + (height - mc.fontRendererObj.FONT_HEIGHT) / 2,
                textCol);
    }
}