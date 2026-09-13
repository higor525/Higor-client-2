package com.higor.client.hud;

import com.higor.client.HigorClient;
import com.higor.client.core.Category;
import com.higor.client.core.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.util.List;

public class HudRenderer {

    private boolean wasLeftDown = false;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (Minecraft.getMinecraft().currentScreen != null) return;

        List<Module> modules = HigorClient.instance.moduleManager
                .getModulesByCategory(Category.HUD);

        // 1) Desenha os HUDs normalmente
        for (Module m : modules) {
            if (!(m instanceof HudModule)) continue;
            HudModule hud = (HudModule) m;
            if (!hud.isEnabled()) continue;

            hud.onRender();
        }

        // 2) Se está no editor, desenha bordas
        if (HudEditor.editorMode) {
            for (Module m : modules) {
                if (!(m instanceof HudModule)) continue;
                HudModule hud = (HudModule) m;
                if (!hud.isEnabled()) continue;
                hud.drawEditorBorder();
            }

            // Dica de uso
            Minecraft mc = Minecraft.getMinecraft();
            mc.fontRendererObj.drawStringWithShadow(
                    "MODO EDITOR - Arraste os HUDs | K para sair",
                    mc.displayWidth / 4 - 100,
                    mc.displayHeight / 4 - 12, 0xFF00CCFF);
        }

        // 3) Detecção de arrastar (fora do mouse event do MC)
        handleMouse();
    }

    private void handleMouse() {
        if (!HudEditor.editorMode) return;

        int mouseX = Mouse.getX() * getScaledWidth() / Minecraft.getMinecraft().displayWidth;
        int mouseY = getScaledHeight() - Mouse.getY() * getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;

        boolean isDown = Mouse.isButtonDown(0);

        if (isDown && !wasLeftDown) {
            // Clique começou
            HudEditor.onMouseClick(mouseX, mouseY, 0);
        } else if (isDown && wasLeftDown) {
            // Arrastando
            HudEditor.onMouseDrag(mouseX, mouseY);
        } else if (!isDown && wasLeftDown) {
            // Soltou
            HudEditor.onMouseRelease();
        }

        wasLeftDown = isDown;
    }

    private int getScaledWidth() {
        return Minecraft.getMinecraft().currentScreen != null
                ? Minecraft.getMinecraft().currentScreen.width
                : (int) (Minecraft.getMinecraft().displayWidth / getScaleFactor());
    }

    private int getScaledHeight() {
        return Minecraft.getMinecraft().currentScreen != null
                ? Minecraft.getMinecraft().currentScreen.height
                : (int) (Minecraft.getMinecraft().displayHeight / getScaleFactor());
    }

    private double getScaleFactor() {
        return Minecraft.getMinecraft().gameSettings.guiScale == 0
                ? 1.0
                : Minecraft.getMinecraft().gameSettings.guiScale;
    }
}