package com.higor.client.hud;

import com.higor.client.HigorClient;
import com.higor.client.core.Category;
import com.higor.client.core.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class HudEditor {

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState()) return;
        if (Keyboard.getEventKey() == Keyboard.KEY_K) {
            if (Minecraft.getMinecraft().currentScreen == null) {
                HudRenderer.editorMode = !HudRenderer.editorMode;
                System.out.println("[HIGOR CLIENT] Editor HUD: "
                        + (HudRenderer.editorMode ? "ATIVADO" : "DESATIVADO"));
            }
        }
    }
}
