package com.justAm0dd3r.cheatmode.mixins;

import com.justAm0dd3r.cheatmode.events.Hooks;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatListener.class)
public class ChatMixin {
    @Inject(at = @At("HEAD"), method = "handleSystemMessage", cancellable = true)
    public void handleSystemMessage(Component msg, boolean b, CallbackInfo ci) {
        if (msg.getContents() instanceof TranslatableContents contents) {
            if (!Hooks.singleplayer()) if (contents.getKey().equalsIgnoreCase("commands.gamemode.success.self")) ci.cancel();
        }
    }
}
