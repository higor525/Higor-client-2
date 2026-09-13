package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;

import java.lang.reflect.Field;

public class OldAnimations extends Module {

    private final Minecraft mc = Minecraft.getMinecraft();

    private Field equippedProgressField;
    private Field prevEquippedProgressField;
    private boolean reflectionReady = false;

    public OldAnimations() {
        super("OldAnimations", Category.RENDER);

        addSetting(new Setting("Shift Mode", new String[]{"Normal", "Duro", "Lento"}, 0));
        addSetting(new Setting("Sword Anim", true));
        addSetting(new Setting("Food Anim", true));
        addSetting(new Setting("Damage Anim", true));
        addSetting(new Setting("Block Anim", true));
    }

    @Override
    public void onEnable() {
        setupReflection();
        System.out.println("[HIGOR CLIENT] OldAnimations ativado - Shift: " + getMode("Shift Mode"));
    }

    @Override
    public void onDisable() {
        System.out.println("[HIGOR CLIENT] OldAnimations desativado");
    }

    private void setupReflection() {
        try {
            Class<?> clazz = ItemRenderer.class;

            equippedProgressField = getField(clazz, "equippedProgress", "field_178398_f");
            prevEquippedProgressField = getField(clazz, "prevEquippedProgress", "field_178399_g");

            if (equippedProgressField != null && prevEquippedProgressField != null) {
                equippedProgressField.setAccessible(true);
                prevEquippedProgressField.setAccessible(true);
                reflectionReady = true;
            }
        } catch (Exception e) {
            reflectionReady = false;
            System.err.println("[HIGOR CLIENT] OldAnimations: reflection falhou");
        }
    }

    private Field getField(Class<?> clazz, String... names) {
        for (String name : names) {
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {}
        }
        return null;
    }

    @Override
    public void onUpdate() {
        if (!reflectionReady || mc.thePlayer == null) return;

        try {
            ItemRenderer renderer = mc.getItemRenderer();
            if (renderer == null) return;

            float progress = equippedProgressField.getFloat(renderer);
            String mode = getMode("Shift Mode");

            if (mode.equalsIgnoreCase("Duro")) {
                // Animação mais rápida/seca (estilo antigo)
                if (progress < 1.0F) {
                    progress = Math.min(1.0F, progress + 0.45F);
                    equippedProgressField.setFloat(renderer, progress);
                }
            } else if (mode.equalsIgnoreCase("Lento")) {
                if (progress < 1.0F) {
                    progress = Math.min(1.0F, progress + 0.07F);
                    equippedProgressField.setFloat(renderer, progress);
                }
            }
            // Normal = não mexe

        } catch (Exception ignored) {}
    }

    // Métodos úteis pro resto do client
    public String getShiftMode() {
        return getMode("Shift Mode");
    }

    public boolean hasSwordAnim() {
        return getBool("Sword Anim");
    }

    public boolean hasFoodAnim() {
        return getBool("Food Anim");
    }

    public boolean hasDamageAnim() {
        return getBool("Damage Anim");
    }

    public boolean hasBlockAnim() {
        return getBool("Block Anim");
    }
}