package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;

import java.lang.reflect.Field;

public class HurtCam extends Module {

    private Field hurtCameraField = null;
    private boolean fieldChecked = false;

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
        if (mc.thePlayer == null) return;
        if (mc.entityRenderer == null) return;

        // Procura o campo hurtCameraEffect na EntityRenderer (só 1 vez)
        if (!fieldChecked) {
            findHurtCameraField(mc.entityRenderer);
            fieldChecked = true;
        }

        if (hurtCameraField == null) return;

        try {
            // Pega o valor atual
            float current = hurtCameraField.getFloat(mc.entityRenderer);
            if (current == 0.0f) return;

            // Aplica a intensidade configurada
            float intensity = (float) getNumber("Intensidade");
            hurtCameraField.setFloat(mc.entityRenderer, current * intensity);
        } catch (Exception e) {
            // Ignora silenciosamente
        }
    }

    private void findHurtCameraField(Object renderer) {
        // Tenta vários nomes possíveis (MCP mappings)
        String[] possibleNames = {
            "hurtCameraEffect",
            "field_78498_aX",
            "rendererUpdateCount"
        };

        for (String name : possibleNames) {
            try {
                Field f = EntityRenderer.class.getDeclaredField(name);
                f.setAccessible(true);
                hurtCameraField = f;
                System.out.println("[HIGOR CLIENT] HurtCam: campo encontrado: " + name);
                return;
            } catch (NoSuchFieldException e) {
                // Tenta o próximo
            }
        }

        System.out.println("[HIGOR CLIENT] HurtCam: campo NAO encontrado (efeito nao funcionara)");
    }
}