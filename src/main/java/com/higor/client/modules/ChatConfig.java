package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;

public class ChatConfig extends Module {

    public ChatConfig() {
        super("ChatConfig", Category.RENDER);

        // Background do chat
        addSetting(new Setting("Background", new String[]{"Padrao", "Transparente", "Sutil", "Sólido"}, 0));
        addSetting(new Setting("Opacidade", 0.5, 0.0, 1.0, 0.05));
        addSetting(new Setting("Chat Aberto Background", new String[]{"Padrao", "Transparente", "Sutil"}, 0));
    }

    public String getBgMode() {
        return getMode("Background");
    }

    public double getOpacity() {
        return getNumber("Opacidade");
    }

    @Override
    public void onEnable() {
        System.out.println("[HIGOR CLIENT] ChatConfig ativado - BG: " + getBgMode());
    }

    @Override
    public void onDisable() {
        System.out.println("[HIGOR CLIENT] ChatConfig desativado");
    }
}
