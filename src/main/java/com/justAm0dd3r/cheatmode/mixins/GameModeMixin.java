package com.justAm0dd3r.cheatmode.mixins;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.justAm0dd3r.cheatmode.events.Hooks.mc;

@Mixin(MultiPlayerGameMode.class)
public class GameModeMixin {
    @Inject(at = @At("TAIL"), method = "setLocalMode(Lnet/minecraft/world/level/GameType;)V")
    public void setLocalMode(GameType gameMode, CallbackInfo ci) {
        if (mc().player == null) return;

        if (mc().screen instanceof InventoryScreen) mc().forceSetScreen(new CreativeModeInventoryScreen(mc().player));
    }
}
