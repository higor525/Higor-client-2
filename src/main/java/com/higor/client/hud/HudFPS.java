package com.higor.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class HudFPS extends HudModule {

    public HudFPS() {
        super("FPS");
    }

    @Override
    public void onRender() {
        Minecraft mc = Minecraft.getMinecraft();

        int fps = Minecraft.getDebugFPS();
        String text = "FPS: " + fps;

        float x = getPosX();
        float y = getPosY();
        float scale = getScale();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1);

        int textColor = getTextColor();

        if (hasBackground()) {
            int w = mc.fontRendererObj.getStringWidth(text) + 4;
            int h = mc.fontRendererObj.FONT_HEIGHT + 4;
            Gui.drawRect(-2, -2, w - 2, h - 2, getBgColor());
        }

        mc.fontRendererObj.drawStringWithShadow(text, 0, 0, textColor);

        GlStateManager.popMatrix();
    }
}