package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;

/**
 * NoHurtCam — remove o efeito de balançar a câmera ao tomar dano.
 *
 * Funciona sem Mixin:
 *   - Ao ativar, se registra no MinecraftForge.EVENT_BUS.
 *   - A cada frame (RenderTickEvent.START), zera o campo hurtCameraEffect
 *     do EntityRenderer via reflection, antes que o jogo o leia pra
 *     calcular o sway da câmera.
 *   - Ao desativar, se desregistra do bus (sem overhead quando off).
 */
public class NoHurtCam extends Module {

    private Field hurtCameraEffect = null;
    private boolean fieldSearched = false;

    public NoHurtCam() {
        super("NoHurtCam", Category.RENDER);
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        System.out.println("[HIGOR CLIENT] NoHurtCam ativado");
    }

    @Override
