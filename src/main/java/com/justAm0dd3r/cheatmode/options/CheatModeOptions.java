package com.justAm0dd3r.cheatmode.options;

import com.justAm0dd3r.cheatmode.config.Config;
import com.justAm0dd3r.cheatmode.events.Hooks;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.Locale;

import static net.minecraft.client.Options.genericValueLabel;

public class CheatModeOptions {
    /*private static Component percentValueLabel(Component p_231898_, double p_231899_) {
        return Component.translatable("options.percent_value", p_231898_, (int)(p_231899_ * 100.0D));
    }*/

    public static final OptionInstance<Boolean> instantCreativeInventory = OptionInstance.createBoolean(
            "cheatmode.options.instant_creative_inventory",
            Config.CLIENT.instantCreativeInventory.get(),
            value -> {
                Config.CLIENT.instantCreativeInventory.set(value);
                Config.CLIENT_SPEC.save();
            }
    );

    public static final OptionInstance<Boolean> flight = OptionInstance.createBoolean(
            "cheatmode.options.flight",
            Config.CLIENT.flight.get(),
            value -> {
                Config.CLIENT.flight.set(value);
                Config.CLIENT_SPEC.save();
                if (!value && Hooks.mc().player != null) Hooks.mc().player.getAbilities().flying = false;
            }
    );
    public static final OptionInstance<Double> reach = new OptionInstance<>(
        "cheatmode.options.reach",
        OptionInstance.cachedConstantTooltip(Component.translatable("options.cheatmode.reach.tooltip")),
        (title, value) -> genericValueLabel(title, Component.literal(String.format(Locale.ROOT, "%.1f", value*50))),
        OptionInstance.UnitDouble.INSTANCE.xmap(Mth::square, Math::sqrt),
        Config.CLIENT.reach.get(),
        (value) -> {
            Config.CLIENT.reach.set(value*50);
            Config.CLIENT_SPEC.save();
        }
    );

}
