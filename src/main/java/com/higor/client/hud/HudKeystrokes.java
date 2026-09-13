package com.higor.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

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
        int boxSize = 16;
        int gap = 2;

        // W
        drawKey(mc, "W", Keyboard.isKeyDown(Keyboard.KEY_W),
                0, 0, boxSize, bg, textColor);

        // A
        drawKey(mc, "A", Keyboard.isKeyDown(Keyboard.KEY_A),
                -(boxSize + gap), boxSize + gap, boxSize, bg, textColor);

        // S
        drawKey(mc, "S", Keyboard.isKeyDown(Keyboard.KEY_S),
                0, boxSize + gap, boxSize, bg, textColor);

        // D
        drawKey(mc, "D", Keyboard.isKeyDown(Keyboard.KEY_D),
                boxSize + gap, boxSize + gap, boxSize, bg, textColor);

        // LMB
        boolean lmb = org.lwjgl.input.Mouse.isButtonDown(0);
        drawKeyWide(mc, "LMB", lmb, -(boxSize + gap), (boxSize + gap) * 2,
                boxSize * 2 + gap, bg, textColor);

        // RMB
        boolean rmb = org.lwjgl.input.Mouse.isButtonDown(1);
        drawKeyWide(mc, "RMB", rmb, boxSize / 2 + gap,
                (boxSize + gap) * 2, boxSize * 2 + gap, bg, textColor);

        GlStateManager.popMatrix();
    }

    private void drawKey(Minecraft mc, String label, boolean pressed,
                         int x, int y, int size, int bg, int textColor) {
        int color = pressed ? 0xFFFFFFFF : bg;
        int textCol = pressed ? 0xFF000000 : textColor;

        if (hasBackground() || pressed) {
            drawRect(x, y, x + size, y + size, color);
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
            drawRect(x, y, x + width, y + height, color);
        }

        mc.fontRendererObj.drawString(label,
                x + (width - mc.fontRendererObj.getStringWidth(label)) / 2,
                y + (height - mc.fontRendererObj.FONT_HEIGHT) / 2,
                textCol);
    }
                                      }
