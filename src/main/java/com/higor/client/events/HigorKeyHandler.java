package com.higor.client.events;

import com.higor.client.gui.HigorClickGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class HigorKeyHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            if (key == Keyboard.KEY_RSHIFT) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.currentScreen == null) {
                    mc.displayGuiScreen(new HigorClickGui());
                }
            }
        }
    }
}
