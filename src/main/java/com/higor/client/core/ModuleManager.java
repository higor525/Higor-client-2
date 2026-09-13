package com.higor.client.core;

import com.higor.client.hud.HudCPS;
import com.higor.client.hud.HudFPS;
import com.higor.client.hud.HudKeystrokes;
import com.higor.client.hud.HudPotion;
import com.higor.client.hud.HudSidebar;
import com.higor.client.modules.ChatConfig;
import com.higor.client.modules.EntityCulling;
import com.higor.client.modules.FPSBoost;
import com.higor.client.modules.FullBright;
import com.higor.client.modules.HitDelayFix;
import com.higor.client.modules.HurtCam;
import com.higor.client.modules.MotoG15Profile;
import com.higor.client.modules.MouseDelayFix;
import com.higor.client.modules.NoParticles;
import com.higor.client.modules.OldAnimations;
import com.higor.client.modules.Performance;
import com.higor.client.modules.Perspective;
import com.higor.client.modules.ToggleSprint;
import com.higor.client.modules.Zoom;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<Module>();

    public void registerAll() {
        System.out.println("[HIGOR CLIENT] Registrando modulos...");

        // ==== MOVEMENT ====
        register(new ToggleSprint());

        // ==== COMBAT ====
        register(new HitDelayFix());

        // ==== RENDER ====
        register(new FullBright());
        register(new OldAnimations());
        register(new HurtCam());
        register(new Zoom());
        register(new Perspective());
        register(new ChatConfig());

        // ==== PERFORMANCE ====
        register(new MouseDelayFix());
        register(new Performance());
        register(new FPSBoost());
        register(new EntityCulling());
        register(new MotoG15Profile());
        register(new NoParticles());

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
            // Salva TODOS os módulos (HUD + invisíveis)
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