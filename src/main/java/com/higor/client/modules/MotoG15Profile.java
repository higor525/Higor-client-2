package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;

public class MotoG15Profile extends Module {

    public MotoG15Profile() {
        super("MotoG15Profile", Category.PERFORMANCE);
        addSetting(new Setting("Render Distance", 6.0, 2.0, 12.0, 1.0));
        addSetting(new Setting("Particulas", new String[]{"Todas", "Diminuidas", "Minimas"}, 2));
        addSetting(new Setting("Nuvens", false));
        addSetting(new Setting("Sombras Entidades", false));
        addSetting(new Setting("Smooth Lighting", false));
        addSetting(new Setting("View Bobbing", false));
        addSetting(new Setting("Mipmap", 0.0, 0.0, 4.0, 1.0));
        addSetting(new Setting("FPS Limit", 120.0, 30.0, 260.0, 10.0));
        addSetting(new Setting("GUI Scale", new String[]{"Auto", "Pequena", "Normal", "Grande"}, 0));
    }

    @Override
    public void onEnable() {
        System.out.println("[HIGOR CLIENT] Perfil Moto G15 aplicado");
    }

    @Override
    public void onDisable() {
        System.out.println("[HIGOR CLIENT] Perfil Moto G15 desativado");
    }

    @Override
    public void onUpdate() {
        // Aplica configs continuamente enquanto ligado
        apply();
    }

    private void apply() {
        Minecraft mc = Minecraft.getMinecraft();

        mc.gameSettings.renderDistanceChunks = (int) getNumber("Render Distance");

        String particles = getMode("Particulas");
        if (particles.equalsIgnoreCase("Minimas")) {
            mc.gameSettings.particleSetting = 2;
        } else if (particles.equalsIgnoreCase("Diminuidas")) {
            mc.gameSettings.particleSetting = 1;
        } else {
            mc.gameSettings.particleSetting = 0;
        }

        mc.gameSettings.clouds = getBool("Nuvens") ? 1 : 0;
        mc.gameSettings.entityShadows = getBool("Sombras Entidades");
        mc.gameSettings.ambientOcclusion = getBool("Smooth Lighting") ? 1 : 0;
        mc.gameSettings.viewBobbing = getBool("View Bobbing");
        mc.gameSettings.mipmapLevels = (int) getNumber("Mipmap");
        mc.gameSettings.limitFramerate = (int) getNumber("FPS Limit");
    }
}
