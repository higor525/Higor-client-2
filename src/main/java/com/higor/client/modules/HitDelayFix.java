package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HitDelayFix extends Module {

    public HitDelayFix() {
        super("HitDelayFix", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        System.out.println("[HIGOR CLIENT] HitDelayFix ativado");
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        System.out.println("[HIGOR CLIENT] HitDelayFix desativado");
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (!isEnabled()) return;
        if (event.entityPlayer == null) return;
        if (event.entityPlayer != Minecraft.getMinecraft().thePlayer) return;

        // Reduz o delay de ataque resetando o ticksSinceLastSwing
        // Campo existe no EntityLivingBase do 1.8.9
        try {
            java.lang.reflect.Field f = net.minecraft.entity.EntityLivingBase.class
                    .getDeclaredField("ticksSinceLastSwing");
            f.setAccessible(true);
            f.setInt(Minecraft.getMinecraft().thePlayer, 0);
        } catch (Exception e) {
            // Se não achar o campo, ignora silenciosamente
        }
    }
}