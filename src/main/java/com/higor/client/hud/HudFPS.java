package com.higor.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class HudFPS extends HudModule {

    public HudFPS() {
        super("FPS");
    }

    @Override
    public void onRender() {
        Minecraft mc = Minecraft.getMinecraft();

        // Pega o FPS atual (já calculado pelo Minecraft)
        int fps = Minecraft.getDebugFPS();
        String text = "FPS: " + fps;

        // Posição e escala
        float x = getPosX();
        float y = getPosY();
        float scale = getScale();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1);

        int textColor = getTextColor();

        // Desenha o background se estiver ativado
        if (hasBackground()) {
            int w = mc.fontRendererObj.getStringWidth(text) + 4;
            int h = mc.fontRendererObj.FONT_HEIGHT + 4;
            drawRect(-2, -2, w - 2, h - 2, getBgColor());
        }

        // Desenha o texto
        mc.fontRendererObj.drawStringWithShadow(text, 0, 0, textColor);

        GlStateManager.popMatrix();
    }
}
