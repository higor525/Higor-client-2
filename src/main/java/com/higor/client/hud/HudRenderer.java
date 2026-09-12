package com.higor.client.hud;

import com.higor.client.HigorClient;
import com.higor.client.core.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class HudRenderer {

    public static boolean editorMode = false;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (Minecraft.getMinecraft().currentScreen != null) return;

        List<Module> modules = HigorClient.instance.moduleManager.getModulesByCategory(
                com.higor.client.core.Category.HUD);

        for (Module m : modules) {
            if (!(m instanceof HudModule)) continue;
            HudModule hud = (HudModule) m;
            if (!hud.isEnabled()) continue;

            // Chama o desenho específico do módulo
            hud.onRender();

            // Se estiver no editor, desenha borda ao redor
            if (editorMode) {
                // A borda é desenhada pelos próprios módulos por enquanto
                // Vai ser melhorada na próxima versão
            }
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.HOTBAR) return;
        // Aqui pode ser otimizado pra desenhar HUD antes da hotbar
    }
    }
