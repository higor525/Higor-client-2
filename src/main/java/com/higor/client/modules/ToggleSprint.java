package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;

public class ToggleSprint extends Module {

    public ToggleSprint() {
        super("ToggleSprint", Category.MOVEMENT);
        // Modos: 0 = Sempre, 1 = Só na frente, 2 = Manual
        addSetting(new Setting("Modo", new String[]{"Sempre", "Frente", "Manual"}, 0));
    }

    public String getMode() {
        return getMode("Modo");
    }

    public boolean isAlwaysSprint() {
        return getMode().equalsIgnoreCase("Sempre");
    }

    public boolean isForwardSprint() {
        return getMode().equalsIgnoreCase("Frente");
    }
}
