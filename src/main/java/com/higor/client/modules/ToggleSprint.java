package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;

public class ToggleSprint extends Module {

    public ToggleSprint() {
        super("ToggleSprint", Category.MOVEMENT);
        addSetting(new Setting("Modo", new String[]{"Sempre", "Frente", "Manual"}, 0));
    }

    @Override
    public void onEnable() {
        System.out.println("[HIGOR CLIENT] ToggleSprint ativado - Modo: " + getMode("Modo"));
    }

    @Override
    public void onDisable() {
        System.out.println("[HIGOR CLIENT] ToggleSprint desativado");
    }

    @Override
    public void onUpdate() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        String mode = getMode("Modo");

        // Não força sprint se o jogador estiver agachado
        if (mc.thePlayer.isSneaking()) return;

        // Não força sprint se não estiver comendo, atirando etc.
        if (mc.thePlayer.isUsingItem()) return;

        if (mode.equalsIgnoreCase("Sempre")) {
            // Sempre sprint (só se estiver com movimento)
            if (isMoving()) {
                mc.thePlayer.setSprinting(true);
            }
        } else if (mode.equalsIgnoreCase("Frente")) {
            // Só sprint quando anda pra frente
            if (mc.gameSettings.keyBindForward.isKeyDown() && isMoving()) {
                mc.thePlayer.setSprinting(true);
            }
        }
        // Modo "Manual" não força nada
    }

    private boolean isMoving() {
        Minecraft mc = Minecraft.getMinecraft();
        return mc.gameSettings.keyBindForward.isKeyDown()
                || mc.gameSettings.keyBindBack.isKeyDown()
                || mc.gameSettings.keyBindLeft.isKeyDown()
                || mc.gameSettings.keyBindRight.isKeyDown();
    }

    public String getMode() {
        return getMode("Modo");
    }

    public boolean isAlwaysSprint() {
        return getMode().equalsIgnoreCase("Sempre");
    }

    public boolean isForwardSprint() {
        return getMode().equalsIgnoreCase("Frente");
    }
}