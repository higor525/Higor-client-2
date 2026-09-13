package com.higor.client.mixin;

import com.higor.client.HigorClient;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat {

    @ModifyArgs(
        method = "drawChat",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"
        )
    )
    private void higorclient$drawChatArgs(Args args) {
        // Verifica se o módulo ChatConfig está ativo
        if (HigorClient.instance == null) return;
        com.higor.client.core.Module chatConfig = HigorClient.instance.moduleManager
                .getModuleByName("ChatConfig");
        if (chatConfig == null || !chatConfig.isEnabled()) return;

        // Modifica a cor do fundo (último arg)
        String mode = ((com.higor.client.modules.ChatConfig) chatConfig).getBgMode();

        if (mode.equalsIgnoreCase("Transparente")) {
            args.set(4, 0x00000000); // totalmente transparente
        } else if (mode.equalsIgnoreCase("Sutil")) {
            args.set(4, 0x40000000); // semi-transparente
        } else if (mode.equalsIgnoreCase("Sólido")) {
            args.set(4, 0xCC000000); // preto sólido
        }
        // "Padrao" não modifica
    }
}
