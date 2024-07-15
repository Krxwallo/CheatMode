package io.github.krxwallo.cheatmode.options;

import io.github.krxwallo.cheatmode.config.Config;
import io.github.krxwallo.cheatmode.events.Hooks;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.Locale;

import static net.minecraft.client.Options.genericValueLabel;

public class CheatModeOptions {

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

    public static final OptionInstance<Double> blockReach = new OptionInstance<>(
        "cheatmode.options.block_reach",
        OptionInstance.cachedConstantTooltip(Component.translatable("options.cheatmode.block_reach.tooltip")),
        (title, value) -> genericValueLabel(title, Component.literal(String.format(Locale.ROOT, "%.1f", value*50))),
        OptionInstance.UnitDouble.INSTANCE.xmap(Mth::square, Math::sqrt),
        Config.CLIENT.blockReach.get()/50,
        (value) -> {
            Config.CLIENT.blockReach.set(value*50);
            Config.CLIENT_SPEC.save();
        }
    );

    public static final OptionInstance<Double> interactionReach = new OptionInstance<>(
            "cheatmode.options.interaction_reach",
            OptionInstance.cachedConstantTooltip(Component.translatable("options.cheatmode.interaction_reach.tooltip")),
            (title, value) -> genericValueLabel(title, Component.literal(String.format(Locale.ROOT, "%.1f", value*50))),
            OptionInstance.UnitDouble.INSTANCE.xmap(Mth::square, Math::sqrt),
            Config.CLIENT.interactionReach.get()/50,
            (value) -> {
                Config.CLIENT.interactionReach.set(value*50);
                Config.CLIENT_SPEC.save();
            }
    );

}
