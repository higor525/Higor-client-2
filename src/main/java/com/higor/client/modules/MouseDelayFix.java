package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;

public class MouseDelayFix extends Module {

    public MouseDelayFix() {
        super("MouseDelayFix", Category.PERFORMANCE);
    }

    @Override
    public void onEnable() {
        System.out.println("[HIGOR CLIENT] MouseDelayFix ativado");
    }

    @Override
    public void onDisable() {
        System.out.println("[HIGOR CLIENT] MouseDelayFix desativado");
    }
}
