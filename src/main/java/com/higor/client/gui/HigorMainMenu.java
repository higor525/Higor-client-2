package com.higor.client.gui;

import com.higor.client.HigorClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiScreen;

public class HigorMainMenu extends GuiScreen {

    private static final int BTN_SINGLE = 1;
    private static final int BTN_MULTI = 2;
    private static final int BTN_OPTIONS = 3;
    private static final int BTN_QUIT = 4;

    @Override
    public void initGui() {
        this.buttonList.clear();
        int centerX = this.width / 2;
        int startY = this.height / 2 - 30;
        int btnW = 200;
        int btnH = 20;
        int gap = 24;

        this.buttonList.add(new HigorButton(BTN_SINGLE, centerX - btnW / 2, startY, btnW, btnH, "SINGLEPLAYER"));
        this.buttonList.add(new HigorButton(BTN_MULTI, centerX - btnW / 2, startY + gap, btnW, btnH, "MULTIPLAYER"));
        this.buttonList.add(new HigorButton(BTN_OPTIONS, centerX - btnW / 2, startY + gap * 2, btnW, btnH, "OPTIONS"));
        this.buttonList.add(new HigorButton(BTN_QUIT, centerX - btnW / 2, startY + gap * 3, btnW, btnH, "QUIT"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, this.width, this.height, 0xFF000000);

        drawRect(0, this.height / 2 - 80, this.width, this.height / 2 - 79, 0xFF00AAFF);
        drawRect(0, this.height / 2 + 70, this.width, this.height / 2 + 71, 0xFF00AAFF);

        this.drawCenteredString(this.fontRendererObj, "HIGOR CLIENT",
                this.width / 2, this.height / 2 - 65, 0xFF00CCFF);

        this.drawString(this.fontRendererObj, "v" + HigorClient.VERSION, 4, this.height - 12, 0xFF00AAFF);
        this.drawString(this.fontRendererObj, "Minecraft 1.8.9  |  Forge", 4, this.height - 22, 0xFF666666);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case BTN_SINGLE:
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case BTN_MULTI:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case BTN_OPTIONS:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case BTN_QUIT:
                this.mc.shutdown();
                break;
        }
    }
}
