package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;

import java.lang.reflect.Field;

public class HurtCam extends Module {

    private Field hurtCameraField = null;
    private Field rendererUpdateCountField = null;
    private boolean fieldChecked = false;
    private int checkCounter = 0;

    public HurtCam() {
        super("HurtCam", Category.RENDER);
        addSetting(new Setting("Intensidade", 0.0, 0.0, 2.0, 0.1));
    }

    @Override
    public void onEnable() {
        System.out.println("[HIGOR CLIENT] HurtCam ativado");
    }

    @Override
    public void onDisable() {
        System.out.println("[HIGOR CLIENT] HurtCam desativado");
    }

    @Override
    public void onUpdate() {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.entityRenderer == null) return;

        // Procura os campos só 1 vez (e tenta vários nomes)
        if (!fieldChecked) {
            findFields(mc.entityRenderer);
            fieldChecked = true;
        }

        if (hurtCameraField == null) return;

        try {
            // A cada tick, se o jogador tomou dano recentemente, zera o efeito
            if (mc.thePlayer.hurtTime > 0) {
                float intensity = (float) getNumber("Intensidade");
                hurtCameraField.setFloat(mc.entityRenderer, intensity * 0.0f);
            }
        } catch (Exception e) {
            // Ignora
        }
    }

    private void findFields(Object renderer) {
        Class<?> clazz = EntityRenderer.class;

        // Tenta todos os nomes conhecidos (MCP + SRG + obfuscated)
        String[][] fieldNames = {
            {"hurtCameraEffect", "field_78498_aX", "a"},
            {"rendererUpdateCount", "field_78529_t", "t"},
            {"cameraYaw", "field_78490_B", "B"},
            {"cameraPitch", "field_78491_C", "C"}
        };

        for (String[] names : fieldNames) {
            for (String name : names) {
                try {
                    Field f = clazz.getDeclaredField(name);
                    f.setAccessible(true);
                    if (name.equals("hurtCameraEffect") || name.equals("field_78498_aX")) {
                        hurtCameraField = f;
                        System.out.println("[HIGOR CLIENT] HurtCam: campo 'hurtCameraEffect' encontrado: " + name);
                        return;
                    }
                } catch (NoSuchFieldException e) {
                    // Tenta próximo
                }
            }
        }

        // Se não achou por nome, lista TODOS os campos pra debug
        System.out.println("[HIGOR CLIENT] HurtCam: campo NAO encontrado. Listando campos disponiveis:");
        for (Field f : clazz.getDeclaredFields()) {
            System.out.println("[HIGOR CLIENT]   - " + f.getName() + " (" + f.getType().getSimpleName() + ")");
        }
    }
}