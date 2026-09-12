package com.higor.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class HigorButton extends GuiButton {

    public HigorButton(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!this.visible) return;

        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition
                && mouseX < this.xPosition + this.width
                && mouseY < this.yPosition + this.height;

        int bg = this.hovered ? 0xFF0A1A2A : 0xFF0A0A0A;
        drawRect(this.xPosition, this.yPosition,
                 this.xPosition + this.width, this.yPosition + this.height, bg);

        int border = 0xFF00AAFF;
        drawRect(this.xPosition, this.yPosition,
                 this.xPosition + this.width, this.yPosition + 1, border);
        drawRect(this.xPosition, this.yPosition + this.height - 1,
                 this.xPosition + this.width, this.yPosition + this.height, border);
        drawRect(this.xPosition, this.yPosition,
                 this.xPosition + 1, this.yPosition + this.height, border);
        drawRect(this.xPosition + this.width - 1, this.yPosition,
                 this.xPosition + this.width, this.yPosition + this.height, border);

        int textColor = this.hovered ? 0xFF00CCFF : 0xFFFFFFFF;
        this.drawCenteredString(mc.fontRendererObj, this.displayString,
                this.xPosition + this.width / 2,
                this.yPosition + (this.height - 8) / 2, textColor);
    }
}
