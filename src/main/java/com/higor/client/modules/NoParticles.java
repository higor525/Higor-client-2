package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;

public class NoParticles extends Module {

    private int originalParticleSetting = 0;

    public NoParticles() {
        super("NoParticles", Category.PERFORMANCE);
        addSetting(new Setting("Ativado", true));
    }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings != null) {
            originalParticleSetting = mc.gameSettings.particleSetting;
            mc.gameSettings.particleSetting = 2; // 2 = Minimal
        }
        System.out.println("[HIGOR CLIENT] NoParticles ativado");
    }

    @Override
    public void onDisable() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings != null) {
            mc.gameSettings.particleSetting = originalParticleSetting;
        }
        System.out.println("[HIGOR CLIENT] NoParticles desativado");
    }

    @Override
    public void onUpdate() {
        if (!isEnabled()) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings != null && mc.gameSettings.particleSetting != 2) {
            mc.gameSettings.particleSetting = 2;
        }
    }
}
