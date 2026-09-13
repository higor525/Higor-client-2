package com.higor.client.hud;

import com.higor.client.HigorClient;
import com.higor.client.core.Category;
import com.higor.client.core.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.List;

public class HudEditor {

    // Estado do editor
    public static boolean editorMode = false;
    private static HudModule draggingModule = null;
    private static float dragOffsetX = 0;
    private static float dragOffsetY = 0;

    // ================================================
    // TOGGLE DO MODO EDITOR (tecla K)
    // ================================================
    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState()) return;
        if (Keyboard.getEventKey() == Keyboard.KEY_K) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.currentScreen == null) {
                editorMode = !editorMode;
                draggingModule = null;
                System.out.println("[HIGOR CLIENT] Editor HUD: "
                        + (editorMode ? "ATIVADO" : "DESATIVADO"));
                if (!editorMode) {
                    // Salvou ao sair
                    if (HigorClient.instance != null) {
                        HigorClient.instance.configManager.saveAll(
                                HigorClient.instance.moduleManager);
                    }
                }
            }
        }
    }

    // ================================================
    // DETECÇÃO DE CLIQUE (só roda no editor)
    // ================================================
    public static void onMouseClick(int mouseX, int mouseY, int button) {
        if (!editorMode || button != 0) return;

        Minecraft mc = Minecraft.getMinecraft();
        float scale = 1.0f;

        List<Module> mods = HigorClient.instance.moduleManager
                .getModulesByCategory(Category.HUD);

        // Procura módulo clicado (de cima pra baixo)
        for (int i = mods.size() - 1; i >= 0; i--) {
            Module m = mods.get(i);
            if (!(m instanceof HudModule)) continue;
            HudModule hud = (HudModule) m;
            if (!hud.isEnabled()) continue;

            float x = hud.getPosX();
            float y = hud.getPosY();
            int w = hud.getWidth();
            int h = hud.getHeight();

            if (mouseX >= x && mouseX <= x + w * scale
                    && mouseY >= y && mouseY <= y + h * scale) {
                draggingModule = hud;
                dragOffsetX = mouseX - x;
                dragOffsetY = mouseY - y;
                return;
            }
        }
    }

    // ================================================
    // ARRASTAR (chamado a cada frame com mouse pressionado)
    // ================================================
    public static void onMouseDrag(int mouseX, int mouseY) {
        if (!editorMode || draggingModule == null) return;
        draggingModule.setPosX(mouseX - dragOffsetX);
        draggingModule.setPosY(mouseY - dragOffsetY);
    }

    // ================================================
    // SOLTAR (salva a posição)
    // ================================================
    public static void onMouseRelease() {
        if (!editorMode || draggingModule == null) return;
        draggingModule = null;
        // Salva imediatamente
        if (HigorClient.instance != null) {
            HigorClient.instance.configManager.saveAll(
                    HigorClient.instance.moduleManager);
        }
    }

    public static boolean isEditorMode() { return editorMode; }
}