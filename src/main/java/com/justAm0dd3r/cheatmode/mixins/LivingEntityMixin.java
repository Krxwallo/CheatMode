package com.justAm0dd3r.cheatmode.mixins;

import com.justAm0dd3r.cheatmode.config.Config;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CancellationException;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = LivingEntity.class)
public class LivingEntityMixin {
    // reach distance = block placing, breaking, interactions
    // attack range = attacking entities

    @Shadow @Final private static Logger LOGGER;

    @Inject(at = @At("HEAD"), method = "getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D", locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    public void getAttributeValue(Attribute attribute, CallbackInfoReturnable<Double> cir) {
        if (!Config.CLIENT_SPEC.isLoaded()) return;

        try {
            if (attribute == NeoForgeMod.ENTITY_REACH.value() || attribute == NeoForgeMod.BLOCK_REACH.value())
                cir.setReturnValue(Config.CLIENT.reach.get());
        } catch (CancellationException e) {
            LOGGER.warn("Failed to get attribute value for " + attribute + "!", e);
        }
    }
}
