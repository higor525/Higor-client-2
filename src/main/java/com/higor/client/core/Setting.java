package com.higor.client.core;

public class Setting {

    public enum Type {
        BOOLEAN, NUMBER, MODE, COLOR
    }

    private final String name;
    private final Type type;

    // Boolean
    private boolean boolValue;

    // Number
    private double numValue;
    private final double min, max, step;

    // Mode
    private String[] modes;
    private int modeIndex;

    // Color (ARGB)
    private int colorValue;

    // ==== CONSTRUTORES ====

    public Setting(String name, boolean value) {
        this.name = name;
        this.type = Type.BOOLEAN;
        this.boolValue = value;
        this.min = 0; this.max = 0; this.step = 0;
    }

    public Setting(String name, double value, double min, double max, double step) {
        this.name = name;
        this.type = Type.NUMBER;
        this.numValue = value;
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public Setting(String name, String[] modes, int defaultIndex) {
        this.name = name;
        this.type = Type.MODE;
        this.modes = modes;
        this.modeIndex = defaultIndex;
        this.min = 0; this.max = 0; this.step = 0;
    }

    public Setting(String name, int color) {
        this.name = name;
        this.type = Type.COLOR;
        this.colorValue = color;
        this.min = 0; this.max = 0; this.step = 0;
    }

    // ==== GETTERS BÁSICOS ====

    public String getName() { return name; }
    public Type getType() { return type; }

    // ==== BOOLEAN ====
    public boolean getBool() { return boolValue; }
    public void setBool(boolean v) { this.boolValue = v; }
    public void toggleBool() { this.boolValue = !this.boolValue; }

    // ==== NUMBER ====
    public double getNumber() { return numValue; }
    public void setNumber(double v) {
        if (v < min) v = min;
        if (v > max) v = max;
        this.numValue = v;
    }
    public void addNumber(double amount) {
        setNumber(numValue + amount);
    }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getStep() { return step; }

    // ==== MODE ====
    public String[] getModes() { return modes; }
    public int getModeIndex() { return modeIndex; }
    public String getMode() { return modes[modeIndex]; }
    public void setModeIndex(int i) {
        if (i < 0) i = 0;
        if (i >= modes.length) i = modes.length - 1;
        this.modeIndex = i;
    }
    public void nextMode() {
        modeIndex++;
        if (modeIndex >= modes.length) modeIndex = 0;
    }
    public boolean isMode(String mode) { return getMode().equalsIgnoreCase(mode); }

    // ==== COLOR ====
    public int getColor() { return colorValue; }
    public void setColor(int c) { this.colorValue = c; }
    }
