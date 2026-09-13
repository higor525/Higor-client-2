package com.higor.client.core;

import com.higor.client.hud.*;
import com.higor.client.modules.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<Module>();

    public void registerAll() {
        register(new ToggleSprint());
        register(new HitDelayFix());
        register(new FullBright());
        register(new OldAnimations());
        register(new HurtCam());
        register(new NoHurtCam());
        register(new Zoom());
        register(new Perspective());
        register(new ChatConfig());
        register(new MouseDelayFix());
        register(new Performance());
        register(new FPSBoost());
        register(new EntityCulling());
        register(new MotoG15Profile());
        register(new NoParticles());
        register(new HudFPS());
        register(new HudCPS());
        register(new HudKeystrokes());
        register(new HudPotion());
        register(new HudSidebar());
    }

    public void register(Module module) { modules.add(module); }
    public List<Module> getModules() { return modules; }

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
