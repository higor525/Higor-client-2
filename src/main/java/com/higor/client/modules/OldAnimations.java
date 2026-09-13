package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;

public class OldAnimations extends Module {

    public OldAnimations() {
        super("OldAnimations", Category.RENDER);

        // Configurações de cada aspecto
        addSetting(new Setting("Shift Mode", new String[]{"Normal", "Duro", "Lento"}, 0));
        addSetting(new Setting("Sword Anim", true));
        addSetting(new Setting("Food Anim", true));
        addSetting(new Setting("Damage Anim", true));
        addSetting(new Setting("Block Anim", true));
    }

    public String getShiftMode() {
        return getMode("Shift Mode");
    }

    public boolean hasSwordAnim() {
        return getBool("Sword Anim");
    }

    public boolean hasFoodAnim() {
        return getBool("Food Anim");
    }

    public boolean hasDamageAnim() {
        return getBool("Damage Anim");
    }

    public boolean hasBlockAnim() {
        return getBool("Block Anim");
    }

    @Override
    public void onEnable() {
        System.out.println("[HIGOR CLIENT] OldAnimations ativado - Shift: " + getShiftMode());
    }

    @Override
    public void onDisable() {
        System.out.println("[HIGOR CLIENT] OldAnimations desativado");
    }
}
