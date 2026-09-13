package com.higor.client.hud;

import com.higor.client.HigorClient;
import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.gui.HigorClickGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class HudRenderer {

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        Minecraft mc = Minecraft.getMinecraft();

        // Só NÃO desenha se tiver uma GUI aberta que NÃO seja a ClickGUI
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

            // Se estiver na ClickGUI, desenha borda pra indicar que pode arrastar
            if (mc.currentScreen instanceof HigorClickGui) {
                hud.drawEditorBorder();
            }
        }
    }
}