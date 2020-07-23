package com.justAm0dd3r.cheatmode;

import com.justAm0dd3r.cheatmode.events.Events;
import com.justAm0dd3r.cheatmode.reference.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: justAm0dd3r
 */
@Mod(Reference.MOD_ID)
public class CheatMode
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static Events events = null;

    public CheatMode() {
        LOGGER.info(Reference.MOD_NAME + " Version " + Reference.VERSION + " by " + Reference.AUTHOR + " started up.");

        MinecraftForge.EVENT_BUS.register(CheatMode.events = new Events());
    }

    @SuppressWarnings("unused")
    public static Events getEvents() {
        return events;
    }
}
