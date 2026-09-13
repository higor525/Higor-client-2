package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;

public class FullBright extends Module {

    private float originalGamma = 1.0f;
    private boolean applied = false;

    public FullBright() {
        super("FullBright", Category.RENDER);
        addSetting(new Setting("Intensidade", 1.0, 0.0, 1.0, 0.1));
    }

    @Override
    public void onEnable() {
        System.out.println("[HIGOR CLIENT] FullBright ativado");
    }

    @Override
    public void onDisable() {
        if (applied) {
            Minecraft.getMinecraft().gameSettings.gammaSetting = originalGamma;
            applied = false;
        }
        System.out.println("[HIGOR CLIENT] FullBright desativado");
    }

    @Override
    public void onUpdate() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        if (!applied) {
            originalGamma = mc.gameSettings.gammaSetting;
            applied = true;
        }

        // gamma 1.0 = noite, 100.0 = brilho máximo
        float intensity = (float) getNumber("Intensidade");
        float targetGamma = originalGamma + (intensity * 10.0f);

        mc.gameSettings.gammaSetting = targetGamma;
    }

    public float getIntensity() {
        return (float) getNumber("Intensidade");
    }
}