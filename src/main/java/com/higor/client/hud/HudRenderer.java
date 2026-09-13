package com.higor.client.hud;

import com.higor.client.HigorClient;
import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.gui.HigorClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
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

    // Cancela a renderização da sidebar vanilla se o HIGOR estiver substituindo
    @SubscribeEvent
    public void onRenderPre(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return;

        Module sidebar = HigorClient.instance.moduleManager.getModuleByName("Sidebar");
        if (!(sidebar instanceof HudSidebar)) return;
        HudSidebar hs = (HudSidebar) sidebar;

        if (hs.isEnabled() && hs.shouldReplaceVanilla()) {
            // Cancela a renderização vanilla da sidebar
            // Não dá pra cancelar "só" a sidebar, então escondemos via display slot
            ScoreObjective obj = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            if (obj != null) {
                // Salva o nome original e esconde temporariamente
                // Estratégia: limpar o display slot 1 temporariamente
                mc.theWorld.getScoreboard().setObjectiveInDisplaySlot(1, null);
                // Restaura depois (no Post)
                pendingRestore = obj;
            }
        }
    }

    private ScoreObjective pendingRestore = null;

    @SubscribeEvent
    public void onRenderPost(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (pendingRestore == null) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld != null) {
            mc.theWorld.getScoreboard().setObjectiveInDisplaySlot(1, pendingRestore);
        }
        pendingRestore = null;
    }
}