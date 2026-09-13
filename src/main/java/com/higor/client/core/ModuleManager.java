package com.higor.client.core;

import com.higor.client.hud.HudCPS;
import com.higor.client.hud.HudFPS;
import com.higor.client.hud.HudKeystrokes;
import com.higor.client.hud.HudPotion;
import com.higor.client.hud.HudSidebar;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<Module>();

    public void registerAll() {
        System.out.println("[HIGOR CLIENT] Registrando modulos...");

        // ==== MOVEMENT ====
        register(new Module("ToggleSprint", Category.MOVEMENT));

        // ==== RENDER ====
        register(new Module("FullBright", Category.RENDER));
        register(new Module("OldAnimations", Category.RENDER));

        // ==== HUD ====
        register(new HudFPS());
        register(new HudCPS());
        register(new HudKeystrokes());
        register(new HudPotion());
        register(new HudSidebar());

        System.out.println("[HIGOR CLIENT] " + modules.size() + " modulos registrados.");
    }

    public void register(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModulesByCategory(Category category) {
        List<Module> result = new ArrayList<Module>();
        for (Module m : modules) {
            if (m.getCategory() == category) result.add(m);
        }
        return result;
    }

    public Module getModuleByName(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }
}