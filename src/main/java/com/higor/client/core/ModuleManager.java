package com.higor.client.core;

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
        // Os módulos HUD serão adicionados na 4.4

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