package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;

public class FPSBoost extends Module {

    private boolean applied = false;
    private int originalParticles;

    public FPSBoost() {
        super("FPSBoost", Category.PERFORMANCE);
        addSetting(new Setting("Reduzir Particulas", true));
        addSetting(new Setting("Remover Nuvens", true));
        addSetting(new Setting("Desligar Sombras Entidades", true));
        addSetting(new Setting("Desligar Smooth Lighting", true));
    }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getMinecraft();
        originalParticles = mc.gameSettings.particleSetting;
        apply();
        applied = true;
        System.out.println("[HIGOR CLIENT] FPSBoost ativado");
    }

    @Override
    public void onDisable() {
        if (!applied) return;
        Minecraft mc = Minecraft.getMinecraft();
        mc.gameSettings.particleSetting = originalParticles;
        mc.gameSettings.saveOptions();
        applied = false;
        System.out.println("[HIGOR CLIENT] FPSBoost desativado");
    }

    @Override
    public void onUpdate() {
        if (applied && isEnabled()) {
            apply();
        }
    }

    private void apply() {
        Minecraft mc = Minecraft.getMinecraft();

        if (getBool("Reduzir Particulas")) {
            mc.gameSettings.particleSetting = 2; // Minimal
        }
        if (getBool("Remover Nuvens")) {
            mc.gameSettings.clouds = 0;
        }
        if (getBool("Desligar Sombras Entidades")) {
            mc.gameSettings.entityShadows = false;
        }
        if (getBool("Desligar Smooth Lighting")) {
            mc.gameSettings.ambientOcclusion = 0;
        }
    }
}
