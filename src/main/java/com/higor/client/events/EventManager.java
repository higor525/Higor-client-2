package com.higor.client.events;

import com.higor.client.HigorClient;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventManager {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (HigorClient.instance != null && HigorClient.instance.moduleManager != null) {
                for (int i = 0; i < HigorClient.instance.moduleManager.getModules().size(); i++) {
                    HigorClient.instance.moduleManager.getModules().get(i).onUpdate();
                }
            }
        }
    }
}
