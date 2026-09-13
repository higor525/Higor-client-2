
package com.higor.client;

import com.higor.client.config.ConfigManager;
import com.higor.client.core.ModuleManager;
import com.higor.client.events.EventManager;
import com.higor.client.gui.GuiManager;
import com.higor.client.hud.HudManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = HigorClient.MODID, name = HigorClient.NAME, version = HigorClient.VERSION)
public class HigorClient {

    public static final String MODID = "higorclient";
    public static final String NAME = "HIGOR CLIENT";
    public static final String VERSION = "0.1.0";

    @Mod.Instance(MODID)
    public static HigorClient instance;

    public ModuleManager moduleManager;
    public ConfigManager configManager;
    public EventManager eventManager;
    public GuiManager guiManager;
    public HudManager hudManager;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("[HIGOR CLIENT] Pre-Init iniciando...");
        this.configManager = new ConfigManager(event.getModConfigurationDirectory());
        this.eventManager = new EventManager();
        this.moduleManager = new ModuleManager();
        this.guiManager = new GuiManager();
        this.hudManager = new HudManager();
        MinecraftForge.EVENT_BUS.register(this.eventManager);
        MinecraftForge.EVENT_BUS.register(new com.higor.client.events.MenuEventHandler());
        MinecraftForge.EVENT_BUS.register(new com.higor.client.events.HigorKeyHandler());
        MinecraftForge.EVENT_BUS.register(new com.higor.client.hud.HudRenderer());
        MinecraftForge.EVENT_BUS.register(new com.higor.client.hud.HudEditor());
        MinecraftForge.EVENT_BUS.register(new com.higor.client.events.ModuleEventManager());
        System.out.println("[HIGOR CLIENT] Pre-Init concluido.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("[HIGOR CLIENT] Init iniciando...");
        this.moduleManager.registerAll();
        this.configManager.loadAll(this.moduleManager);
        System.out.println("[HIGOR CLIENT] Core " + VERSION + " inicializado");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("[HIGOR CLIENT] Post-Init concluido.");
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        // Salva configs ao sair do mundo
        if (configManager != null && moduleManager != null) {
            configManager.saveAll(moduleManager);
        }
    }
}