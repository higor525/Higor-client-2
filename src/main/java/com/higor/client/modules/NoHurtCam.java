package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import java.lang.reflect.Field;

public class NoHurtCam extends Module {
    private Field hurtCameraEffect = null;
    private boolean fieldSearched = false;

    public NoHurtCam() {
        super("NoHurtCam", Category.RENDER);
    }

    @Override public void onEnable() { MinecraftForge.EVENT_BUS.register(this); }
    @Override public void onDisable() { MinecraftForge.EVENT_BUS.unregister(this); }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.entityRenderer == null) return;
        if (!fieldSearched) { findField(); fieldSearched = true; }
        if (hurtCameraEffect == null) return;
        try { hurtCameraEffect.setFloat(mc.entityRenderer, 0.0f); }
        catch (Exception ignored) {}
    }

    private void findField() {
        try {
            hurtCameraEffect = ReflectionHelper.findField(
                EntityRenderer.class, "hurtCameraEffect", "field_78498_aX"
            );
        } catch (Exception ignored) {}
    }
}
