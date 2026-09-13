package com.higor.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class HudCPS extends HudModule {

    private static final List<Long> leftClicks = new ArrayList<Long>();
    private static final List<Long> rightClicks = new ArrayList<Long>();
    private static boolean lastLeft = false;
    private static boolean lastRight = false;

    public HudCPS() {
        super("CPS");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        boolean leftNow = Mouse.isButtonDown(0);
        boolean rightNow = Mouse.isButtonDown(1);

        if (leftNow && !lastLeft) leftClicks.add(System.currentTimeMillis());
        if (rightNow && !lastRight) rightClicks.add(System.currentTimeMillis());

        lastLeft = leftNow;
        lastRight = rightNow;

        cleanOld(leftClicks);
        cleanOld(rightClicks);
    }

    private void cleanOld(List<Long> list) {
        long now = System.currentTimeMillis();
        while (!list.isEmpty() && now - list.get(0) > 1000) {
            list.remove(0);
        }
    }

    @Override
    public void onRender() {
        Minecraft mc = Minecraft.getMinecraft();
        String text = "CPS: " + leftClicks.size() + " | " + rightClicks.size();

        GlStateManager.pushMatrix();
        GlStateManager.translate(getPosX(), getPosY(), 0);
        GlStateManager.scale(getScale(), getScale(), 1);

        if (hasBackground()) {
            int w = mc.fontRendererObj.getStringWidth(text) + 4;
            int h = mc.fontRendererObj.FONT_HEIGHT + 4;
            Gui.drawRect(-2, -2, w - 2, h - 2, getBgColor());
        }

        mc.fontRendererObj.drawStringWithShadow(text, 0, 0, getTextColor());

        GlStateManager.popMatrix();
    }
}