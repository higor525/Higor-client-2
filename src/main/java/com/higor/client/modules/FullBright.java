package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;

public class FullBright extends Module {

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
        System.out.println("[HIGOR CLIENT] FullBright desativado");
        // Restaura o gamma original
        Minecraft.getMinecraft().gameSettings.gammaSetting = 1.0f;
    }

    public float getIntensity() {
        return (float) getNumber("Intensidade");
    }
}
