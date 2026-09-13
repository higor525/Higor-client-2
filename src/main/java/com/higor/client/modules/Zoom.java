package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class Zoom extends Module {

    private boolean zooming = false;
    private float originalFov = 70.0f;

    public Zoom() {
        super("Zoom", Category.RENDER);
        addSetting(new Setting("Nivel", 4.0, 2.0, 10.0, 0.5));
        addSetting(new Setting("Tecla", new String[]{"Z", "C", "V", "X"}, 0));
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        System.out.println("[HIGOR CLIENT] Zoom ativado");
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        System.out.println("[HIGOR CLIENT] Zoom desativado");
    }

    public double getZoomLevel() {
        return getNumber("Nivel");
    }

    private int getZoomKey() {
        String mode = getMode("Tecla");
        if (mode.equalsIgnoreCase("Z")) return Keyboard.KEY_Z;
        if (mode.equalsIgnoreCase("C")) return Keyboard.KEY_C;
        if (mode.equalsIgnoreCase("V")) return Keyboard.KEY_V;
        if (mode.equalsIgnoreCase("X")) return Keyboard.KEY_X;
        return Keyboard.KEY_Z;
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (Minecraft.getMinecraft().currentScreen != null) return;
        int key = getZoomKey();

        if (Keyboard.getEventKey() == key) {
            if (Keyboard.getEventKeyState()) {
                if (!zooming) {
                    zooming = true;
                    originalFov = Minecraft.getMinecraft().gameSettings.fovSetting;
                    Minecraft.getMinecraft().gameSettings.fovSetting =
                            (float) (originalFov / getZoomLevel());
                }
            } else {
                if (zooming) {
                    zooming = false;
                    Minecraft.getMinecraft().gameSettings.fovSetting = originalFov;
                }
            }
        }
    }
                                     }
