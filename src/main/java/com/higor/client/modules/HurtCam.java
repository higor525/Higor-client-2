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
