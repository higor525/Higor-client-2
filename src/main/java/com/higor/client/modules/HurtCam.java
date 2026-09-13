package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HurtCam extends Module {

    public HurtCam() {
        super("HurtCam", Category.RENDER);
        addSetting(new Setting("Intensidade", 1.0, 0.0, 2.0, 0.1));
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        System.out.println("[HIGOR CLIENT] HurtCam ativado");
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        System.out.println("[HIGOR CLIENT] HurtCam desativado");
    }

    public float getIntensity() {
        return (float) getNumber("Intensidade");
    }
}
