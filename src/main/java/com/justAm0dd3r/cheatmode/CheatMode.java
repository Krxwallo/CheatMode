package com.justAm0dd3r.cheatmode;

import com.justAm0dd3r.cheatmode.config.Config;
import com.justAm0dd3r.cheatmode.reference.Reference;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

/**
 * Author: justAm0dd3r
 */
@Mod(Reference.MOD_ID)
public class CheatMode {
    public CheatMode() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC, Reference.MOD_ID + "-client.toml");
    }
}
