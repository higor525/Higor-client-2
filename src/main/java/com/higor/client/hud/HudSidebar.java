package com.higor.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HudSidebar extends HudModule {

    public HudSidebar() {
        super("Sidebar");
    }

    @Override
    public void onRender() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return;

        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) return;

        GlStateManager.pushMatrix();
        GlStateManager.translate(getPosX(), getPosY(), 0);
        GlStateManager.scale(getScale(), getScale(), 1);

        Collection<Score> scores = scoreboard.getSortedScores(objective);
        List<Score> list = new ArrayList<Score>();
        for (Score s : scores) {
            if (s.getPlayerName() != null
                    && !s.getPlayerName().startsWith("#")) {
                list.add(s);
            }
        }

        if (list.size() > 15) {
            list = list.subList(list.size() - 15, list.size());
        }

        // Título
        String title = objective.getDisplayName();
        int titleWidth = mc.fontRendererObj.getStringWidth(title);
        int maxWidth = titleWidth;

        for (Score s : list) {
            String line = getLineText(scoreboard, s);
            int w = mc.fontRendererObj.getStringWidth(line);
            if (w > maxWidth) maxWidth = w;
        }

        int padding = 3;
        int lineH = mc.fontRendererObj.FONT_HEIGHT + 1;
        int totalH = lineH * (list.size() + 1) + padding * 2 + 2;
        int totalW = maxWidth + padding * 2;

        // Background (estilo 1.8 - fundo escuro semi-transparente)
        drawRect(0, 0, totalW, totalH, getBgColor());

        // Título (centralizado, com linha azul)
        mc.fontRendererObj.drawString(title,
                (totalW - titleWidth) / 2, padding, getTextColor());
        drawRect(0, padding + lineH, totalW, padding + lineH + 1, 0xFF00AAFF);

        // Linhas
        int y = padding + lineH + 2;
        for (int i = list.size() - 1; i >= 0; i--) {
            Score s = list.get(i);
            String line = getLineText(scoreboard, s);
            mc.fontRendererObj.drawString(line, padding, y, 0xFFFFFFFF);
            y += lineH;
        }

        GlStateManager.popMatrix();
    }

    private String getLineText(Scoreboard scoreboard, Score score) {
        ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
        String prefix = team != null ? team.getColorPrefix() : "";
        String suffix = team != null ? team.getColorSuffix() : "";
        return prefix + score.getPlayerName() + suffix;
    }
}
