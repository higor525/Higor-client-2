package com.higor.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;

public class HudPotion extends HudModule {

    public HudPotion() {
        super("PotionStatus");
    }

    @Override
    public void onRender() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        Collection<PotionEffect> effects = mc.thePlayer.getActivePotionEffects();
        if (effects.isEmpty()) return;

        GlStateManager.pushMatrix();
        GlStateManager.translate(getPosX(), getPosY(), 0);
        GlStateManager.scale(getScale(), getScale(), 1);

        int y = 0;
        for (PotionEffect effect : effects) {
            String name = effect.getEffectName();
            // Pega só o nome curto (ex: "potion.damageBoost" -> "damageBoost")
            if (name.startsWith("potion.")) name = name.substring(7);

            int duration = effect.getDuration() / 20;
            int min = duration / 60;
            int sec = duration % 60;
            String time = String.format("%d:%02d", min, sec);

            String text = name + " " + time;
            int color = effect.getPotionID() >= 0
                    ? 0xFFAA00FF : getTextColor();

            if (hasBackground()) {
                int w = mc.fontRendererObj.getStringWidth(text) + 4;
                int h = mc.fontRendererObj.FONT_HEIGHT + 2;
                drawRect(-2, y - 1, w - 2, y + h - 1, getBgColor());
            }

            mc.fontRendererObj.drawStringWithShadow(text, 0, y, color);
            y += mc.fontRendererObj.FONT_HEIGHT + 2;
        }

        GlStateManager.popMatrix();
    }
}
