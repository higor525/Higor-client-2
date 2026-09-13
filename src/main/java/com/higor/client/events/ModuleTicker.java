package com.higor.client.events;

import com.higor.client.HigorClient;
import com.higor.client.core.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ModuleTicker {

    private int tickCounter = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (HigorClient.instance == null) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        tickCounter++;

        for (Module m : HigorClient.instance.moduleManager.getModules()) {
            if (m.isEnabled()) {
                try {
                    m.onUpdate();
                } catch (Exception e) {
                    System.err.println("[HIGOR CLIENT] Erro em " + m.getName() + ": " + e.getMessage());
                }
            }
        }
    }
}
