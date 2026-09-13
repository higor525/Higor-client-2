package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class Perspective extends Module {

    public Perspective() {
        super("Perspective", Category.RENDER);
        addSetting(new Setting("Tecla", new String[]{"P", "B", "N", "M"}, 0));
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        System.out.println("[HIGOR CLIENT] Perspective ativado");
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        System.out.println("[HIGOR CLIENT] Perspective desativado");
    }

    private int getKey() {
        String mode = getMode("Tecla");
        if (mode.equalsIgnoreCase("P")) return Keyboard.KEY_P;
        if (mode.equalsIgnoreCase("B")) return Keyboard.KEY_B;
        if (mode.equalsIgnoreCase("N")) return Keyboard.KEY_N;
        if (mode.equalsIgnoreCase("M")) return Keyboard.KEY_M;
        return Keyboard.KEY_P;
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (Minecraft.getMinecraft().currentScreen != null) return;
        if (Keyboard.getEventKey() != getKey()) return;
        if (!Keyboard.getEventKeyState()) return;

        int current = Minecraft.getMinecraft().gameSettings.thirdPersonView;
        // Cicla: 0 (1ª) -> 1 (3ª tras) -> 2 (3ª frente) -> 0
        Minecraft.getMinecraft().gameSettings.thirdPersonView = (current + 1) % 3;
    }
}
