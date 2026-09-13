package com.higor.client.events;

import com.higor.client.HigorClient;
import com.higor.client.core.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ModuleEventManager {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (HigorClient.instance == null) return;

        for (Module m : HigorClient.instance.moduleManager.getModules()) {
            if (m.isEnabled()) {
                m.onUpdate();
            }
        }
    }
}
