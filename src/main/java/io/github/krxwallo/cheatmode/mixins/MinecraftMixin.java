package io.github.krxwallo.cheatmode.mixins;

import io.github.krxwallo.cheatmode.events.Hooks;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "stop", at = @At("HEAD"))
    private void onStop(CallbackInfo ci) {
        // fix stuck in creative mode
        if (((Minecraft) (Object) this).screen != null) {
            //noinspection DataFlowIssue
            Hooks.onScreenClose(((Minecraft) (Object) this).screen);
        }
    }
}
