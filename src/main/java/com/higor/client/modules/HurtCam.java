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

    @Override public void onEnable() {}
    @Override public void onDisable() {}

    @Override
    public void onUpdate() {
        if (!isEnabled()) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.entityRenderer == null) return;
        if (!fieldChecked) { findField(); fieldChecked = true; }
        if (hurtCameraField == null) return;
        try {
            if (mc.thePlayer.hurtTime > 0) {
                float v = (float) getNumber("Intensidade");
                hurtCameraField.setFloat(mc.entityRenderer, v);
            }
        } catch (Exception ignored) {}
    }

    private void findField() {
        for (String n : new String[]{"hurtCameraEffect","field_78498_aX"}) {
            try {
                Field f = EntityRenderer.class.getDeclaredField(n);
                f.setAccessible(true);
                hurtCameraField = f;
                return;
            } catch (NoSuchFieldException ignored) {}
        }
    }
}
