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

        // Reseta o contador de "attackCooldown" do jogador
        // Na 1.8.9 isso não é uma feature nativa, mas o método ajuda a reduzir o delay
        // que o servidor aplica quando você bate muito rápido
        Minecraft.getMinecraft().thePlayer.resetCooldown();
    }
}