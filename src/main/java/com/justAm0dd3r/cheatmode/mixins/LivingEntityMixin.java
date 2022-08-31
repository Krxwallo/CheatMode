package com.justAm0dd3r.cheatmode.mixins;

import com.justAm0dd3r.cheatmode.config.Config;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = LivingEntity.class)
public class LivingEntityMixin {
    // reach distance = block placing, breaking, interactions
    // attack range = attacking entities

    @Inject(at = @At("TAIL"), method = "getAttributeValue", locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    public void getValue(Attribute attribute, CallbackInfoReturnable<Double> cir) {
        if (!Config.CLIENT_SPEC.isLoaded()) return;

        if (attribute == ForgeMod.ATTACK_RANGE.orElse(null) || attribute == ForgeMod.REACH_DISTANCE.orElse(null))
            cir.setReturnValue(Config.CLIENT.reach.get());
    }
}
