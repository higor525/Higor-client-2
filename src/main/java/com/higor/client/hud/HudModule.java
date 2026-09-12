package com.higor.client.hud;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;

public abstract class HudModule extends Module {

    public HudModule(String name) {
        super(name, Category.HUD);

        // Configurações padrão de todo módulo HUD
        addSetting(new Setting("Posicao X", 5.0, 0.0, 2000.0, 1.0));
        addSetting(new Setting("Posicao Y", 5.0, 0.0, 2000.0, 1.0));
        addSetting(new Setting("Escala", 1.0, 0.5, 3.0, 0.1));
        addSetting(new Setting("Background", true));
        addSetting(new Setting("Cor Background", 0x80000000));
        addSetting(new Setting("Cor Texto", 0xFFFFFFFF));
    }

    // Helpers pra usar dentro dos módulos
    public float getPosX() { return (float) getNumber("Posicao X"); }
    public float getPosY() { return (float) getNumber("Posicao Y"); }
    public float getScale() { return (float) getNumber("Escala"); }
    public boolean hasBackground() { return getBool("Background"); }
    public int getBgColor() { return getColor("Cor Background"); }
    public int getTextColor() { return getColor("Cor Texto"); }

    public void setPosX(float x) { getSetting("Posicao X").setNumber(x); }
    public void setPosY(float y) { getSetting("Posicao Y").setNumber(y); }

    // Cada módulo HUD implementa o que desenhar
    @Override
    public abstract void onRender();
}
