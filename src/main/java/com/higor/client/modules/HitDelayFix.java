package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;

public class HitDelayFix extends Module {

    public HitDelayFix() {
        super("HitDelayFix", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        System.out.println("[HIGOR CLIENT] HitDelayFix ativado");
    }

    @Override
    public void onDisable() {
        System.out.println("[HIGOR CLIENT] HitDelayFix desativado");
    }
}
