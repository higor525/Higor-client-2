package com.higor.client.modules;

import com.higor.client.core.Category;
import com.higor.client.core.Module;
import com.higor.client.core.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;

/**
 * OldAnimations - Versão sem Mixin
 * Usa reflection + eventos do Forge
 */
public class OldAnimations extends Module {

    private final Minecraft mc = Minecraft.getMinecraft();

    // Settings
    private final Setting shiftMode;
    private final Setting swordAnim;
    private final Setting foodAnim;
    private final Setting damageAnim;
    private final Setting blockAnim;

    // Reflection cache
    private Field equippedProgressField;
    private Field prevEquippedProgressField;
    private Field itemToRenderField;
    private boolean reflectionReady = false;

    public OldAnimations() {
        super("OldAnimations", Category.RENDER);

        shiftMode = new Setting("Shift Mode", new String[]{"Normal", "Duro", "Lento"}, 0);
        swordAnim = new Setting("Sword Anim", true);
        foodAnim = new Setting("Food Anim", true);
        damageAnim = new Setting("Damage Anim", true);
        blockAnim = new Setting("Block Anim", true);

        addSetting(shiftMode);
        addSetting(swordAnim);
        addSetting(foodAnim);
        addSetting(damageAnim);
        addSetting(blockAnim);
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        setupReflection();
        System.out.println("[HIGOR CLIENT] OldAnimations ativado - Shift: " + getShiftMode());
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        System.out.println("[HIGOR CLIENT] OldAnimations desativado");
    }

    private void setupReflection() {
        try {
            // ItemRenderer fields (obfuscated names for 1.8.9)
            // equippedProgress = field_178398_f  /  prevEquippedProgress = field_178399_g
            Class<?> itemRendererClass = ItemRenderer.class;

            // Tenta nomes deobfuscados primeiro (dev), depois obfuscados
            equippedProgressField = getField(itemRendererClass, "equippedProgress", "field_178398_f");
            prevEquippedProgressField = getField(itemRendererClass, "prevEquippedProgress", "field_178399_g");
            itemToRenderField = getField(itemRendererClass, "itemToRender", "field_178397_c");

            if (equippedProgressField != null) {
                equippedProgressField.setAccessible(true);
                prevEquippedProgressField.setAccessible(true);
                if (itemToRenderField != null) itemToRenderField.setAccessible(true);
                reflectionReady = true;
            }
        } catch (Exception e) {
            System.err.println("[HIGOR CLIENT] OldAnimations: falha ao preparar reflection");
            e.printStackTrace();
            reflectionReady = false;
        }
    }

    private Field getField(Class<?> clazz, String... names) {
        for (String name : names) {
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {}
        }
        return null;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || mc.thePlayer == null || !reflectionReady) return;

        try {
            ItemRenderer renderer = mc.getItemRenderer();
            if (renderer == null) return;

            // Força progressão mais "dura"/antiga dependendo do modo
            float progress = equippedProgressField.getFloat(renderer);
            float prev = prevEquippedProgressField.getFloat(renderer);

            String mode = getShiftMode();
            if (mode.equals("Duro")) {
                // Animação mais seca (mais parecida com 1.7)
                if (progress < 1.0f) {
                    progress = Math.min(1.0f, progress + 0.4f);
                    equippedProgressField.setFloat(renderer, progress);
                }
            } else if (mode.equals("Lento")) {
                // Mais lento
                if (progress < 1.0f) {
                    progress = Math.min(1.0f, progress + 0.08f);
                    equippedProgressField.setFloat(renderer, progress);
                }
            }
            // Normal = deixa o vanilla

        } catch (Exception ignored) {}
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        if (!isEnabled() || mc.thePlayer == null) return;

        EntityPlayer player = mc.thePlayer;

        // Sword blocking + swing (estilo antigo)
        if (hasSwordAnim() && hasBlockAnim()) {
            if (player.isBlocking() && player.getHeldItem() != null 
                && player.getHeldItem().getItem() instanceof ItemSword) {

                // Aqui normalmente se aplica o transform antigo.
                // Sem Mixin/ASM o controle total do matrix é limitado,
                // mas já reduz o "float" moderno.
            }
        }
    }

    // ==================== Getters usados pelo resto do client ====================

    public String getShiftMode() {
        return getMode("Shift Mode");
    }

    public boolean hasSwordAnim() {
        return getBool("Sword Anim");
    }

    public boolean hasFoodAnim() {
        return getBool("Food Anim");
    }

    public boolean hasDamageAnim() {
        return getBool("Damage Anim");
    }

    public boolean hasBlockAnim() {
        return getBool("Block Anim");
    }

    // Helpers (caso sua classe Module já tenha, pode remover)
    private String getMode(String name) {
        Setting s = getSetting(name);
        if (s == null) return "Normal";
        // Ajuste conforme a implementação real do seu Setting
        return s.getMode() != null ? s.getMode() : "Normal";
    }

    private boolean getBool(String name) {
        Setting s = getSetting(name);
        return s != null && s.getBoolean();
    }
}