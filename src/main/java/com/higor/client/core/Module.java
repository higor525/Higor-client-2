package com.higor.client.core;

import java.util.ArrayList;
import java.util.List;

public class Module {

    private final String name;
    private final Category category;
    private boolean enabled;
    private int keybind;
    private final List<Setting> settings;

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
        this.enabled = false;
        this.keybind = 0;
        this.settings = new ArrayList<Setting>();
    }

    // ==== GETTERS BÁSICOS ====
    public String getName() { return name; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public void toggle() { setEnabled(!enabled); }

    public int getKeybind() { return keybind; }
    public void setKeybind(int k) { this.keybind = k; }

    // ==== SETTINGS ====
    public List<Setting> getSettings() { return settings; }

    public void addSetting(Setting s) { settings.add(s); }

    public Setting getSetting(String settingName) {
        for (Setting s : settings) {
            if (s.getName().equalsIgnoreCase(settingName)) return s;
        }
        return null;
    }

    public boolean getBool(String settingName) {
        Setting s = getSetting(settingName);
        return s != null && s.getBool();
    }

    public double getNumber(String settingName) {
        Setting s = getSetting(settingName);
        return s != null ? s.getNumber() : 0;
    }

    public String getMode(String settingName) {
        Setting s = getSetting(settingName);
        return s != null ? s.getMode() : "";
    }

    public int getColor(String settingName) {
        Setting s = getSetting(settingName);
        return s != null ? s.getColor() : 0xFFFFFFFF;
    }

    // ==== EVENTOS ====
    public void onEnable() {}
    public void onDisable() {}
    public void onUpdate() {}
    public void onRender() {}
}