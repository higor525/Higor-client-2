package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityCulling extends Module {

    public EntityCulling() {
        super("EntityCulling", Category.PERFORMANCE);
        addSetting(new Setting("Distancia Maxima", 32.0, 8.0, 128.0, 8.0));
        addSetting(new Setting("Ignorar Players", true));
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        System.out.println("[HIGOR CLIENT] EntityCulling ativado");
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        System.out.println("[HIGOR CLIENT] EntityCulling desativado");
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre event) {
        if (!isEnabled()) return;

        Entity entity = event.entity;
        if (entity == null) return;

        // Ignora players se a config estiver ON
        if (getBool("Ignorar Players") && entity instanceof EntityPlayer) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        double distance = mc.thePlayer.getDistanceToEntity(entity);
        double maxDistance = getNumber("Distancia Maxima");

        if (distance > maxDistance) {
            event.setCanceled(true);
        }
    }

    public double getMaxDistance() {
        return getNumber("Distancia Maxima");
    }
}
