package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;

public class Performance extends Module {

    private boolean applied = false;

    private int originalRenderDistance;
    private int originalParticles;
    private boolean originalClouds;
    private boolean originalEntityShadows;
    private int originalSmoothLighting;
    private boolean originalViewBobbing;
    private int originalMipmap;

    public Performance() {
        super("Performance", Category.PERFORMANCE);
        addSetting(new Setting("Render Distance", 6.0, 2.0, 16.0, 1.0));
        addSetting(new Setting("Particulas", new String[]{"Todas", "Diminuidas", "Minimas"}, 2));
        addSetting(new Setting("Nuvens", false));
        addSetting(new Setting("Sombras Entidades", false));
        addSetting(new Setting("Smooth Lighting", false));
        addSetting(new Setting("View Bobbing", false));
        addSetting(new Setting("Mipmap", 0.0, 0.0, 4.0, 1.0));
    }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getMinecraft();

        originalRenderDistance = mc.gameSettings.renderDistanceChunks;
        originalParticles = mc.gameSettings.particleSetting;
        originalClouds = mc.gameSettings.clouds;
        originalEntityShadows = mc.gameSettings.entityShadows;
        originalSmoothLighting = mc.gameSettings.ambientOcclusion;
        originalViewBobbing = mc.gameSettings.viewBobbing;
        originalMipmap = mc.gameSettings.mipmapLevels;

        apply();
        applied = true;
        System.out.println("[HIGOR CLIENT] Performance ativado");
    }

    @Override
    public void onDisable() {
        if (!applied) return;
        Minecraft mc = Minecraft.getMinecraft();

        mc.gameSettings.renderDistanceChunks = originalRenderDistance;
        mc.gameSettings.particleSetting = originalParticles;
        mc.gameSettings.clouds = originalClouds;
        mc.gameSettings.entityShadows = originalEntityShadows;
        mc.gameSettings.ambientOcclusion = originalSmoothLighting;
        mc.gameSettings.viewBobbing = originalViewBobbing;
        mc.gameSettings.mipmapLevels = originalMipmap;
        mc.gameSettings.saveOptions();

        applied = false;
        System.out.println("[HIGOR CLIENT] Performance desativado");
    }

    @Override
    public void onUpdate() {
        if (applied && isEnabled()) {
            apply();
        }
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

        mc.gameSettings.clouds = getBool("Nuvens");
        mc.gameSettings.entityShadows = getBool("Sombras Entidades");
        mc.gameSettings.ambientOcclusion = getBool("Smooth Lighting") ? 1 : 0;
        mc.gameSettings.viewBobbing = getBool("View Bobbing");
        mc.gameSettings.mipmapLevels = (int) getNumber("Mipmap");
    }
          }
