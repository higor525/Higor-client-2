package com.higor.client.gui;

import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.List;

public class HigorConfigPanel {

    private final Module module;
    private int x, y;
    private static final int WIDTH = 180;
    private static final int ROW_H = 14;
    private static final int HEADER_H = 16;
    private static final int BG = 0xFF0A0A0A;
    private static final int BORDER = 0xFF00AAFF;
    private static final int TEXT = 0xFFFFFFFF;
    private static final int TEXT_DIM = 0xFF888888;
    private static final int ACCENT = 0xFF00CCFF;
    private static final int BTN_BG = 0xFF001428;
    private static final int BTN_HOVER = 0xFF003366;

    // Arrastar
    private boolean dragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public HigorConfigPanel(Module module, int x, int y) {
        this.module = module;
        this.x = x;
        this.y = y;
    }

    public Module getModule() { return module; }

    public int getHeight() {
        return HEADER_H + 4 + (module.getSettings().size() * ROW_H) + 20;
    }

    public void render(Minecraft mc, int mouseX, int mouseY) {
        List<Setting> settings = module.getSettings();
        int height = getHeight();

        // Fundo
        Gui.drawRect(x, y, x + WIDTH, y + height, BG);
        drawBorder(x, y, WIDTH, height, BORDER);

        // Header (barra de arrastar)
        int headerColor = dragging ? 0xFF003366 : 0xFF001428;
        Gui.drawRect(x, y, x + WIDTH, y + HEADER_H, headerColor);
        Gui.drawRect(x, y + HEADER_H - 1, x + WIDTH, y + HEADER_H, BORDER);
        mc.fontRendererObj.drawString(module.getName(), x + 6, y + 4, ACCENT);

        // Indicador "arraste aqui"
        mc.fontRendererObj.drawString(":::", x + WIDTH - 42, y + 4, 0xFF666666);

        // Botão fechar [X]
        mc.fontRendererObj.drawString("X", x + WIDTH - 12, y + 4, 0xFFFF5555);

        // Settings
        int sy = y + HEADER_H + 4;
        for (Setting s : settings) {
            renderSetting(mc, s, x + 6, sy, mouseX, mouseY);
            sy += ROW_H;
        }

        // Botão Salvar
        String saveText = "Salvar";
        int btnW = 60;
        int btnX = x + (WIDTH - btnW) / 2;
        int btnY = y + height - 16;
        boolean hover = mouseX >= btnX && mouseX <= btnX + btnW
                && mouseY >= btnY && mouseY <= btnY + 12;
        Gui.drawRect(btnX, btnY, btnX + btnW, btnY + 12, hover ? BTN_HOVER : BTN_BG);
        drawBorder(btnX, btnY, btnW, 12, BORDER);
        mc.fontRendererObj.drawString(saveText,
                btnX + (btnW - mc.fontRendererObj.getStringWidth(saveText)) / 2,
                btnY + 2, ACCENT);
    }

    private void renderSetting(Minecraft mc, Setting s, int sx, int sy,
                                int mouseX, int mouseY) {
        String name = s.getName();
        mc.fontRendererObj.drawString(name, sx, sy + 2, TEXT_DIM);

        switch (s.getType()) {
            case BOOLEAN: {
                String val = s.getBool() ? "[ ON  ]" : "[ OFF ]";
                int color = s.getBool() ? ACCENT : TEXT_DIM;
                int vx = x + WIDTH - 42;
                mc.fontRendererObj.drawString(val, vx, sy + 2, color);
                break;
            }
            case NUMBER: {
                String val = formatNum(s.getNumber());
                int vx = x + WIDTH - 60;
                mc.fontRendererObj.drawString(val, vx, sy + 2, TEXT);

                int bx1 = x + WIDTH - 22;
                boolean h1 = isHover(mouseX, mouseY, bx1, sy, 10, 10);
                Gui.drawRect(bx1, sy, bx1 + 10, sy + 10, h1 ? BTN_HOVER : BTN_BG);
                mc.fontRendererObj.drawString("-", bx1 + 3, sy + 1, ACCENT);

                int bx2 = x + WIDTH - 10;
                boolean h2 = isHover(mouseX, mouseY, bx2, sy, 10, 10);
                Gui.drawRect(bx2, sy, bx2 + 10, sy + 10, h2 ? BTN_HOVER : BTN_BG);
                mc.fontRendererObj.drawString("+", bx2 + 3, sy + 1, ACCENT);
                break;
            }
            case MODE: {
                String val = s.getMode();
                int vx = x + WIDTH - 60;
                mc.fontRendererObj.drawString(val, vx, sy + 2, TEXT);
                break;
            }
            case COLOR: {
                String val = "0x" + Integer.toHexString(s.getColor());
                int vx = x + WIDTH - 60;
                mc.fontRendererObj.drawString(val, vx, sy + 2, TEXT);
                break;
            }
        }
    }

    private String formatNum(double d) {
        if (d == (int) d) return String.valueOf((int) d);
        return String.format("%.1f", d);
    }

    private boolean isHover(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    /**
     * Retorna true se o painel deve ser FECHADO.
     */
    public boolean onClick(int mouseX, int mouseY) {
        int height = getHeight();

        // Fechar [X]
        if (mouseX >= x + WIDTH - 14 && mouseX <= x + WIDTH - 2
                && mouseY >= y + 2 && mouseY <= y + 14) {
            return true;
        }

        // Iniciar arrastar (clicou no header, fora do X)
        if (mouseY >= y && mouseY <= y + HEADER_H
                && mouseX >= x && mouseX <= x + WIDTH - 16) {
            this.dragging = true;
            this.dragOffsetX = mouseX - x;
            this.dragOffsetY = mouseY - y;
            return false;
        }

        // Botões +/-
        int sy = y + HEADER_H + 4;
        for (Setting s : module.getSettings()) {
            if (s.getType() == Setting.Type.NUMBER) {
                int bx1 = x + WIDTH - 22;
                if (isHover(mouseX, mouseY, bx1, sy, 10, 10)) {
                    s.addNumber(-s.getStep());
                    return false;
                }
                int bx2 = x + WIDTH - 10;
                if (isHover(mouseX, mouseY, bx2, sy, 10, 10)) {
                    s.addNumber(s.getStep());
                    return false;
                }
            }
            else if (s.getType() == Setting.Type.BOOLEAN) {
                int vx = x + WIDTH - 42;
                if (isHover(mouseX, mouseY, vx, sy, 40, 10)) {
                    s.toggleBool();
                    return false;
                }
            }
            sy += ROW_H;
        }
        return false;
    }

    /**
     * Chamado quando o mouse está pressionado — move o painel.
     */
    public void onMouseDrag(int mouseX, int mouseY) {
        if (dragging) {
            this.x = mouseX - dragOffsetX;
            this.y = mouseY - dragOffsetY;
        }
    }

    /**
     * Chamado quando solta o mouse.
     */
    public void onMouseRelease() {
        this.dragging = false;
    }

    private void drawBorder(int x, int y, int w, int h, int color) {
        Gui.drawRect(x, y, x + w, y + 1, color);
        Gui.drawRect(x, y + h - 1, x + w, y + h, color);
        Gui.drawRect(x, y, x + 1, y + h, color);
        Gui.drawRect(x + w - 1, y, x + w, y + h, color);
    }
}