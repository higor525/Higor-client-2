package com.higor.client.core;

import java.util.ArrayList;
import java.util.List;

public class Module {

    private final String name;
    private final Category category;
    private boolean enabled;
    private int keybind;
    private final List<ModuleSetting> settings;

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
        this.enabled = false;
        this.keybind = 0;
        this.settings = new ArrayList<ModuleSetting>();
    }

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

    public List<ModuleSetting> getSettings() { return settings; }
    public void addSetting(ModuleSetting s) { settings.add(s); }

    public void onEnable() {}
    public void onDisable() {}
    public void onUpdate() {}

    // ==================== MODULE SETTING ====================
    public static class ModuleSetting {
        private final String name;
        private boolean boolValue;
        private double numValue;
        private double min, max;
        private final SettingType type;

        public ModuleSetting(String name, boolean value) {
            this.name = name;
            this.boolValue = value;
            this.type = SettingType.BOOLEAN;
        }

        public ModuleSetting(String name, double value, double min, double max) {
            this.name = name;
            this.numValue = value;
            this.min = min;
            this.max = max;
            this.type = SettingType.NUMBER;
        }

        public String getName() { return name; }
        public SettingType getType() { return type; }
        public boolean getBool() { return boolValue; }
        public void setBool(boolean v) { this.boolValue = v; }
        public double getNumber() { return numValue; }
        public void setNumber(double v) {
            if (v < min) v = min;
            if (v > max) v = max;
            this.numValue = v;
        }
        public double getMin() { return min; }
        public double getMax() { return max; }
    }

    public enum SettingType {
        BOOLEAN, NUMBER
    }
}