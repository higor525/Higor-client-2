package com.higor.client.hud;

import com.higor.client.HigorClient;
import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.gui.HigorClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class HudRenderer {

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.currentScreen != null && !(mc.currentScreen instanceof HigorClickGui)) {
            return;
        }

        List<Module> modules = HigorClient.instance.moduleManager
                .getModulesByCategory(Category.HUD);

        for (Module m : modules) {
            if (!(m instanceof HudModule)) continue;
            HudModule hud = (HudModule) m;
            if (!hud.isEnabled()) continue;

            hud.onRender();

            if (mc.currentScreen instanceof HigorClickGui) {
                hud.drawEditorBorder();
            }
        }
    }

    @SubscribeEvent
    public void onRenderPre(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return;

        Module sidebar = HigorClient.instance.moduleManager.getModuleByName("Sidebar");
        if (!(sidebar instanceof HudSidebar)) return;
        HudSidebar hs = (HudSidebar) sidebar;

        if (!hs.isEnabled() || !hs.shouldReplaceVanilla()) return;

        // Se há uma sidebar vanilla sendo renderizada, cancela
        ScoreObjective obj = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
        if (obj != null) {
            event.setCanceled(true);
        }
    }
}