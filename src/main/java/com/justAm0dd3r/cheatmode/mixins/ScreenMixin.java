package com.justAm0dd3r.cheatmode.mixins;

import com.justAm0dd3r.cheatmode.events.Hooks;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(at = @At("TAIL"), method = "init(Lnet/minecraft/client/Minecraft;II)V")
    public void init(Minecraft mc, int i, int j, CallbackInfo ci) {
        Hooks.onScreenInit((Screen) (Object) this);
    }

    @Inject(at = @At("TAIL"), method = "onClose")
    public void close(CallbackInfo ci) {
        Hooks.onScreenClose((Screen) (Object) this);
    }

    @WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;changeFocus(Lnet/minecraft/client/gui/ComponentPath;)V"), method = "keyPressed")
    private boolean switchFocus(Screen instance, ComponentPath componentPath) {
        // Fix arrow keys not working correctly in chat screen
        //noinspection ConstantValue
        return !(((Object) this) instanceof ChatScreen);
    }
}