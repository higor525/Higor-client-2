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

    private int guiX, guiY;

    @Override
    public void initGui() {
        this.guiX = (this.width - GUI_W) / 2;
        this.guiY = (this.height - GUI_H) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, this.width, this.height, 0x80000000);

        // Painel principal
        drawRect(guiX, guiY, guiX + GUI_W, guiY + GUI_H, PANEL_BG);
        drawBorder(guiX, guiY, GUI_W, GUI_H, PANEL_BORDER);

        // Header
        drawRect(guiX, guiY, guiX + GUI_W, guiY + HEADER_H, 0xFF001428);
        drawRect(guiX, guiY + HEADER_H - 1, guiX + GUI_W, guiY + HEADER_H, PANEL_BORDER);
        drawCenteredString(this.fontRendererObj, "HIGOR CLIENT",
                guiX + GUI_W / 2, guiY + 4, TITLE_COLOR);

        // Sidebar
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

        // Lista de módulos
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

                String toggle = m.isEnabled() ? "[ ON  ]" : "[ OFF ]";
                int toggleColor = m.isEnabled() ? MODULE_ON : MODULE_OFF;
                this.fontRendererObj.drawString(toggle,
                        guiX + GUI_W - 60, modY, toggleColor);

                modY += ROW_H;
            }
        }

        drawString(this.fontRendererObj, "RSHIFT para fechar",
                guiX + 4, guiY + GUI_H - 10, 0xFF444444);

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Painel de config por cima
        if (openPanel != null) {
            openPanel.render(this.mc, mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton != 0) return;

        // Se tem painel aberto, ele captura o clique primeiro
        if (openPanel != null) {
            boolean shouldClose = openPanel.onClick(mouseX, mouseY);
            if (shouldClose) {
                openPanel = null;
            }
            return;
        }

        // Clicou nas categorias
        int catY = guiY + HEADER_H + 4;
        for (Category cat : Category.values()) {
            if (isInSidebar(mouseX, mouseY, catY)) {
                this.selected = cat;
                return;
            }
            catY += ROW_H;
        }

        // Clicou nos módulos
        List<Module> mods = HigorClient.instance.moduleManager.getModulesByCategory(selected);
        int modY = guiY + HEADER_H + 4;
        int modX = guiX + SIDEBAR_W + 6;
        for (Module m : mods) {
            if (isInModuleList(mouseX, mouseY, modX, modY)) {
                // Se o módulo tem configs, abre painel
                if (!m.getSettings().isEmpty()) {
                    openPanel = new HigorConfigPanel(m,
                            guiX + GUI_W + 6, guiY);
                    // Ajusta se ultrapassar a tela
                    if (openPanel != null) {
                        int panelRight = guiX + GUI_W + 6 + 180;
                        if (panelRight > this.width - 4) {
                            openPanel = new HigorConfigPanel(m,
                                    guiX - 186, guiY);
                        }
                    }
                } else {
                    // Senão, só toggla
                    m.toggle();
                }
                return;
            }
            modY += ROW_H;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
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
protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    if (openPanel != null) {
        openPanel.onMouseDrag(mouseX, mouseY);
    }
}

@Override
protected void mouseReleased(int mouseX, int mouseY, int state) {
    super.mouseReleased(mouseX, mouseY, state);
    if (openPanel != null) {
        openPanel.onMouseRelease();
    }
}

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}