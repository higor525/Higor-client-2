
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
    private final File hudFile;
    private final File modulesFile;
    private final Gson gson;

    public ConfigManager(File minecraftConfigDir) {
        this.configDir = new File(minecraftConfigDir, "higorclient");
        this.hudFile = new File(configDir, "hud.json");
        this.modulesFile = new File(configDir, "modules.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        if (!configDir.exists()) {
            boolean created = configDir.mkdirs();
            System.out.println("[HIGOR CLIENT] Pasta config criada: " + created);
        }
    }

    // ==== LOAD ====
    public void loadAll(com.higor.client.core.ModuleManager moduleManager) {
        System.out.println("[HIGOR CLIENT] Carregando configuracoes...");

        // Cria arquivos padrão se não existirem
        createIfMissing("modules.json", "{}");
        createIfMissing("hud.json", "{}");
        createIfMissing("settings.json", "{\"theme\":\"dark-blue\",\"version\":\"0.1.0\"}");
        createIfMissing("profiles.json", "{\"active\":\"default\"}");

        // Carrega os módulos
        loadModules(moduleManager);

        System.out.println("[HIGOR CLIENT] Configuracoes carregadas.");
    }

    private void loadModules(com.higor.client.core.ModuleManager mm) {
        if (!hudFile.exists()) return;

        try {
            FileReader reader = new FileReader(hudFile);
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
            System.out.println("[HIGOR CLIENT] Configuracoes HUD carregadas.");
        } catch (Exception e) {
            System.err.println("[HIGOR CLIENT] Erro ao carregar configs: " + e.getMessage());
        }
    }

    // ==== SAVE ====
    public void saveAll(com.higor.client.core.ModuleManager mm) {
        saveModules(mm);
    }

    private void saveModules(com.higor.client.core.ModuleManager mm) {
        JsonObject root = new JsonObject();

        for (Module m : mm.getModules()) {
            if (m.getCategory() != Category.HUD) continue;

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
            root.add(m.getName(), mod);
        }

        try {
            FileWriter writer = new FileWriter(hudFile);
            gson.toJson(root, writer);
            writer.close();
            System.out.println("[HIGOR CLIENT] Configuracoes HUD salvas.");
        } catch (IOException e) {
            System.err.println("[HIGOR CLIENT] Erro ao salvar configs: " + e.getMessage());
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