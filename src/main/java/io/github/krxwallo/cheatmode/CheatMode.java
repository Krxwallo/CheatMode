package io.github.krxwallo.cheatmode;

import io.github.krxwallo.cheatmode.config.Config;
import io.github.krxwallo.cheatmode.reference.Reference;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

/**
 * Author: Krxwallo
 */
@Mod(Reference.MOD_ID)
public class CheatMode {
    public CheatMode(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC, Reference.MOD_ID + "-client.toml");
    }
}
