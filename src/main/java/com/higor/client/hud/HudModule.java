package com.higor.client.hud;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public abstract class HudModule extends Module {

    public HudModule(String name) {
        super(name, Category.HUD);

        addSetting(new Setting("Posicao X", 5.0, 0.0, 2000.0, 1.0));
        addSetting(new Setting("Posicao Y", 5.0, 0.0, 2000.0, 1.0));
        addSetting(new Setting("Escala", 1.0, 0.5, 3.0, 0.1));
        addSetting(new Setting("Background", true));
        addSetting(new Setting("Cor Background", 0x80000000));
        addSetting(new Setting("Cor Texto", 0xFFFFFFFF));
    }

    // ==== Helpers ====
    public float getPosX() { return (float) getNumber("Posicao X"); }
    public float getPosY() { return (float) getNumber("Posicao Y"); }
    public float getScale() { return (float) getNumber("Escala"); }
    public boolean hasBackground() { return getBool("Background"); }
    public int getBgColor() { return getColor("Cor Background"); }
    public int getTextColor() { return getColor("Cor Texto"); }

    public void setPosX(float x) { getSetting("Posicao X").setNumber(x); }
    public void setPosY(float y) { getSetting("Posicao Y").setNumber(y); }

    // ==== Dimensões (usado pelo editor) ====
    // Por padrão: caixa de 60x12. Sobrescrito por cada módulo se quiser.
    public int getWidth() {
        return 60;
    }

    public int getHeight() {
        return 12;
    }

    /**
     * Desenha uma borda ao redor do módulo (só no modo editor).
     */
    public void drawEditorBorder() {
        int w = (int) (getWidth() * getScale());
        int h = (int) (getHeight() * getScale());
        int x = (int) getPosX();
        int y = (int) getPosY();

        int color = 0xFF00AAFF;
        Gui.drawRect(x - 1, y - 1, x + w + 1, y, color);       // topo
        Gui.drawRect(x - 1, y + h, x + w + 1, y + h + 1, color); // baixo
        Gui.drawRect(x - 1, y, x, y + h, color);                 // esquerda
        Gui.drawRect(x + w, y, x + w + 1, y + h, color);         // direita
    }

    @Override
    public abstract void onRender();
}