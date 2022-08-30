package com.justAm0dd3r.cheatmode.config;

import com.justAm0dd3r.cheatmode.reference.Reference;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Author: justAm0dd3r
 */
public class Config {
    public static class Client {

        public final ForgeConfigSpec.BooleanValue instantCreativeInventory;
/*        public final ForgeConfigSpec.BooleanValue doubleSpeed;*/

        public Client(ForgeConfigSpec.Builder builder) {

            builder.comment("Cheat Mode Mod Configurations | NO RESTART REQUIRED")
                   .push(Reference.MOD_ID);

            instantCreativeInventory = builder
                    .comment("Instantly open the creative inventory (true) or open " +
                            "the survival inventory with a button to open the creative inventory (false)? (default: true)")
                    .worldRestart()
                    .define("instant_creative_inventory", true);
/*
            doubleSpeed = builder
                    .comment("Walk and fly twice as fast? (default: false)")
                    .worldRestart()
                    .define("double_speed", false);
*/
            builder.pop();
        }
    }

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;
    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }
}
