package com.higor.client.gui;

import com.higor.client.HigorClient;
import com.higor.client.core.Category;
import com.higor.client.core.Module;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.List;

public class HigorClickGui extends GuiScreen {

    private Category selected = Category.COMBAT;
    private HigorConfigPanel openPanel = null;
// Arrastar HUDs
private com.higor.client.hud.HudModule draggingHud = null;
private float hudDragOffsetX = 0;
private float hudDragOffsetY = 0;
    private static final int PANEL_BG = 0xFF0A0A0A;
    private static final int PANEL_BORDER = 0xFF00AAFF;
    private static final int SIDEBAR_BG = 0xFF050505;
    private static final int CAT_NORMAL = 0xFFAAAAAA;
    private static final int CAT_HOVER = 0xFFFFFFFF;
    private static final int CAT_SELECTED = 0xFF00CCFF;
    private static final int MODULE_ON = 0xFF00CCFF;
    private static final int MODULE_OFF = 0xFF666666;
    private static final int TITLE_COLOR = 0xFF00CCFF;

    private static final int GUI_W = 320;
    private static final int GUI_H = 200;
    private static final int SIDEBAR_W = 80;
    private static final int HEADER_H = 16;
    private static final int ROW_H = 12;
    private static final int PANEL_W = 180;

    private int guiX, guiY;

    @Override
    public void initGui() {
        this.guiX = (this.width - GUI_W) / 2;
        this.guiY = (this.height - GUI_H) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, this.width, this.height, 0x80000000);

        drawRect(guiX, guiY, guiX + GUI_W, guiY + GUI_H, PANEL_BG);
        drawBorder(guiX, guiY, GUI_W, GUI_H, PANEL_BORDER);

        drawRect(guiX, guiY, guiX + GUI_W, guiY + HEADER_H, 0xFF001428);
        drawRect(guiX, guiY + HEADER_H - 1, guiX + GUI_W, guiY + HEADER_H, PANEL_BORDER);
        drawCenteredString(this.fontRendererObj, "HIGOR CLIENT",
                guiX + GUI_W / 2, guiY + 4, TITLE_COLOR);

        drawRect(guiX, guiY + HEADER_H, guiX + SIDEBAR_W, guiY + GUI_H, SIDEBAR_BG);
        drawRect(guiX + SIDEBAR_W - 1, guiY + HEADER_H, guiX + SIDEBAR_W, guiY + GUI_H, PANEL_BORDER);

        int catY = guiY + HEADER_H + 4;
        for (Category cat : Category.values()) {
            boolean isSelected = cat == selected;
            boolean hovered = isInSidebar(mouseX, mouseY, catY);

            int color = isSelected ? CAT_SELECTED : (hovered ? CAT_HOVER : CAT_NORMAL);
            if (isSelected) {
                drawRect(guiX + 2, catY - 1, guiX + SIDEBAR_W - 2, catY + 9, 0xFF001428);
            }
            this.fontRendererObj.drawString(cat.getDisplayName(), guiX + 6, catY, color);
            catY += ROW_H;
        }

        List<Module> mods = HigorClient.instance.moduleManager.getModulesByCategory(selected);
        int modY = guiY + HEADER_H + 4;
        int modX = guiX + SIDEBAR_W + 6;

        if (mods.isEmpty()) {
            this.fontRendererObj.drawString("Em breve...", modX, modY, 0xFF444444);
        } else {
            for (Module m : mods) {
                boolean hovered = isInModuleList(mouseX, mouseY, modX, modY);
                int nameColor = hovered ? 0xFFFFFFFF : 0xFFCCCCCC;
                this.fontRendererObj.drawString(m.getName(), modX, modY, nameColor);

                // Toggle [ ON ] / [ OFF ] — clicável separadamente
                String toggle = m.isEnabled() ? "[ ON  ]" : "[ OFF ]";
                int toggleColor = m.isEnabled() ? MODULE_ON : MODULE_OFF;
                int toggleX = guiX + GUI_W - 60;
                this.fontRendererObj.drawString(toggle, toggleX, modY, toggleColor);

                // Botão de config "cfg" — só nos que têm settings
                if (!m.getSettings().isEmpty()) {
                    int cfgX = guiX + GUI_W - 14;
                    boolean cfgHover = mouseX >= cfgX && mouseX <= cfgX + 12
                            && mouseY >= modY - 1 && mouseY <= modY + 9;
                    int cfgColor = cfgHover ? 0xFFFFFFFF : 0xFF00AAFF;
                    this.fontRendererObj.drawString("cfg", cfgX, modY, cfgColor);
                }

                modY += ROW_H;
            }
        }

        drawString(this.fontRendererObj, "RSHIFT para fechar",
                guiX + 4, guiY + GUI_H - 10, 0xFF444444);

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (openPanel != null) {
            openPanel.render(this.mc, mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton != 0) return;

        // Painel aberto tem prioridade
// PRIORIDADE 0: tentar arrastar um HUD clicado
java.util.List<com.higor.client.core.Module> huds = 
        HigorClient.instance.moduleManager.getModulesByCategory(
                com.higor.client.core.Category.HUD);
for (int i = huds.size() - 1; i >= 0; i--) {
    com.higor.client.core.Module mm = huds.get(i);
    if (!(mm instanceof com.higor.client.hud.HudModule)) continue;
    com.higor.client.hud.HudModule hud = (com.higor.client.hud.HudModule) mm;
    if (!hud.isEnabled()) continue;

    float hx = hud.getPosX();
    float hy = hud.getPosY();
    int hw = (int)(hud.getWidth() * hud.getScale());
    int hh = (int)(hud.getHeight() * hud.getScale());

    if (mouseX >= hx && mouseX <= hx + hw
            && mouseY >= hy && mouseY <= hy + hh) {
        draggingHud = hud;
        hudDragOffsetX = mouseX - hx;
        hudDragOffsetY = mouseY - hy;
        return;
    }
}
        if (openPanel != null) {
            boolean shouldClose = openPanel.onClick(mouseX, mouseY);
            if (shouldClose) {
                openPanel = null;
            }
            return;
        }

        // Categorias
        int catY = guiY + HEADER_H + 4;
        for (Category cat : Category.values()) {
            if (isInSidebar(mouseX, mouseY, catY)) {
                this.selected = cat;
                return;
            }
            catY += ROW_H;
        }

        // Módulos
        List<Module> mods = HigorClient.instance.moduleManager.getModulesByCategory(selected);
        int modY = guiY + HEADER_H + 4;
        int modX = guiX + SIDEBAR_W + 6;
        for (Module m : mods) {
            if (isInModuleList(mouseX, mouseY, modX, modY)) {

                // Clicou no botão "cfg" → abre painel
                if (!m.getSettings().isEmpty()) {
                    int cfgX = guiX + GUI_W - 14;
                    if (mouseX >= cfgX && mouseX <= cfgX + 12) {
                        int px = guiX + GUI_W + 6;
                        if (px + PANEL_W > this.width - 4) {
                            px = guiX - PANEL_W - 6;
                            if (px < 4) {
                                px = guiX + GUI_W - PANEL_W - 4;
                            }
                        }
                        openPanel = new HigorConfigPanel(m, px, guiY);
                        return;
                    }
                }

                // Clicou no [ ON ] / [ OFF ] → toggla
                int toggleX = guiX + GUI_W - 60;
                if (mouseX >= toggleX && mouseX <= toggleX + 50) {
                    m.toggle();
                    return;
                }

                // Clicou no nome → toggla também (facilita)
                m.toggle();
                return;
            }
            modY += ROW_H;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

    // Arrastar painel de config
    if (openPanel != null) {
        openPanel.onMouseDrag(mouseX, mouseY);
        return;
    }

    // Arrastar HUD
    if (draggingHud != null) {
        draggingHud.setPosX(mouseX - hudDragOffsetX);
        draggingHud.setPosY(mouseY - hudDragOffsetY);
    }
}

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (openPanel != null) {
            openPanel.onMouseRelease();
        }
    }

    private boolean isInSidebar(int mouseX, int mouseY, int catY) {
        return mouseX >= guiX && mouseX <= guiX + SIDEBAR_W
                && mouseY >= catY - 1 && mouseY <= catY + 9;
    }

    private boolean isInModuleList(int mouseX, int mouseY, int modX, int modY) {
        return mouseX >= modX && mouseX <= guiX + GUI_W - 4
                && mouseY >= modY - 1 && mouseY <= modY + 9;
    }

    private void drawBorder(int x, int y, int w, int h, int color) {
        drawRect(x, y, x + w, y + 1, color);
        drawRect(x, y + h - 1, x + w, y + h, color);
        drawRect(x, y, x + 1, y + h, color);
        drawRect(x + w - 1, y, x + w, y + h, color);
    }

@Override
public void onGuiClosed() {
    super.onGuiClosed();
    if (HigorClient.instance != null
            && HigorClient.instance.configManager != null
            && HigorClient.instance.moduleManager != null) {
        HigorClient.instance.configManager.saveAll(
                HigorClient.instance.moduleManager);
    }
}

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}