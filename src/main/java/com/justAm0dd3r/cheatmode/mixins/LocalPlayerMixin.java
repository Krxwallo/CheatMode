package com.justAm0dd3r.cheatmode.mixins;

import com.justAm0dd3r.cheatmode.config.Config;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Abilities;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Abilities;mayfly:Z", opcode = Opcodes.GETFIELD), method = "aiStep")
    public boolean mayfly(Abilities instance) {
        return Config.CLIENT.flight.get();
    }
}
