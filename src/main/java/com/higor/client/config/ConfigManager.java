package com.higor.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.higor.client.core.ModuleManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {

    private final File configDir;
    private final Gson gson;

    public ConfigManager(File minecraftConfigDir) {
        this.configDir = new File(minecraftConfigDir, "higorclient");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        if (!configDir.exists()) {
            boolean created = configDir.mkdirs();
            System.out.println("[HIGOR CLIENT] Pasta config criada: " + created);
        }
    }

    public void loadAll(ModuleManager moduleManager) {
        System.out.println("[HIGOR CLIENT] Carregando configuracoes...");
        createIfMissing("modules.json", "{}");
        createIfMissing("hud.json", "{}");
        createIfMissing("settings.json", "{\"theme\":\"dark-blue\",\"version\":\"0.1.0\"}");
        createIfMissing("profiles.json", "{\"active\":\"default\"}");
        System.out.println("[HIGOR CLIENT] Configuracoes carregadas.");
    }

    public void saveAll(ModuleManager moduleManager) {
        System.out.println("[HIGOR CLIENT] Salvando configuracoes...");
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

    public File getConfigDir() {
        return configDir;
    }

    public Gson getGson() {
        return gson;
    }
}
