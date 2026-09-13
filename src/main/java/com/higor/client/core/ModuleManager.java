package com.higor.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {

    private final File configDir;
    private final File modulesFile;
    private final File hudFile;
    private final Gson gson;

    public ConfigManager(File minecraftConfigDir) {
        this.configDir = new File(minecraftConfigDir, "higorclient");
        this.modulesFile = new File(configDir, "modules.json");
        this.hudFile = new File(configDir, "hud.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        if (!configDir.exists()) {
            boolean created = configDir.mkdirs();
            System.out.println("[HIGOR CLIENT] Pasta config criada: " + created);
        }
    }

    public void loadAll(com.higor.client.core.ModuleManager moduleManager) {
        System.out.println("[HIGOR CLIENT] Carregando configuracoes...");

        createIfMissing("modules.json", "{}");
        createIfMissing("hud.json", "{}");
        createIfMissing("settings.json", "{\"theme\":\"dark-blue\",\"version\":\"0.1.0\"}");
        createIfMissing("profiles.json", "{\"active\":\"default\"}");

        loadFromFile(modulesFile, moduleManager);
        loadFromFile(hudFile, moduleManager);

        System.out.println("[HIGOR CLIENT] Configuracoes carregadas.");
    }

    private void loadFromFile(File file, com.higor.client.core.ModuleManager mm) {
        if (!file.exists()) return;

        try {
            FileReader reader = new FileReader(file);
            JsonElement el = new JsonParser().parse(reader);
            reader.close();

            if (!el.isJsonObject()) return;
            JsonObject root = el.getAsJsonObject();

            for (Module m : mm.getModules()) {
                if (!root.has(m.getName())) continue;
                JsonObject mod = root.getAsJsonObject(m.getName());

                if (mod.has("enabled")) {
                    m.setEnabled(mod.get("enabled").getAsBoolean());
                }

                for (Setting s : m.getSettings()) {
                    if (!mod.has(s.getName())) continue;
                    JsonElement sEl = mod.get(s.getName());

                    switch (s.getType()) {
                        case BOOLEAN:
                            s.setBool(sEl.getAsBoolean());
                            break;
                        case NUMBER:
                            s.setNumber(sEl.getAsDouble());
                            break;
                        case MODE:
                            s.setModeIndex(sEl.getAsInt());
                            break;
                        case COLOR:
                            s.setColor(sEl.getAsInt());
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[HIGOR CLIENT] Erro ao carregar " + file.getName() + ": " + e.getMessage());
        }
    }

    public void saveAll(com.higor.client.core.ModuleManager mm) {
        JsonObject modulesObj = new JsonObject();
        JsonObject hudObj = new JsonObject();

        for (Module m : mm.getModules()) {
            JsonObject mod = new JsonObject();
            mod.addProperty("enabled", m.isEnabled());

            for (Setting s : m.getSettings()) {
                switch (s.getType()) {
                    case BOOLEAN:
                        mod.addProperty(s.getName(), s.getBool());
                        break;
                    case NUMBER:
                        mod.addProperty(s.getName(), s.getNumber());
                        break;
                    case MODE:
                        mod.addProperty(s.getName(), s.getModeIndex());
                        break;
                    case COLOR:
                        mod.addProperty(s.getName(), s.getColor());
                        break;
                }
            }

            if (m.getCategory() == Category.HUD) {
                hudObj.add(m.getName(), mod);
            } else {
                modulesObj.add(m.getName(), mod);
            }
        }

        writeJson(modulesFile, modulesObj);
        writeJson(hudFile, hudObj);
        System.out.println("[HIGOR CLIENT] Configuracoes salvas.");
    }

    private void writeJson(File file, JsonObject obj) {
        try {
            FileWriter writer = new FileWriter(file);
            gson.toJson(obj, writer);
            writer.close();
        } catch (IOException e) {
            System.err.println("[HIGOR CLIENT] Erro ao salvar " + file.getName() + ": " + e.getMessage());
        }
    }

    private void createIfMissing(String fileName, String defaultContent) {
        File file = new File(configDir, fileName);
        if (!file.exists()) {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(defaultContent);
                writer.close();
                System.out.println("[HIGOR CLIENT] Criado: " + fileName);
            } catch (IOException e) {
                System.err.println("[HIGOR CLIENT] Erro ao criar " + fileName + ": " + e.getMessage());
            }
        }
    }

    public File getConfigDir() { return configDir; }
    public Gson getGson() { return gson; }
}