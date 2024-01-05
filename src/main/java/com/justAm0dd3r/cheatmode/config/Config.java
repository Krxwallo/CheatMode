package com.justAm0dd3r.cheatmode.config;

import com.justAm0dd3r.cheatmode.reference.Reference;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Author: Krxwallo
 */
public class Config {
    public static class Client {

        public final ModConfigSpec.BooleanValue instantCreativeInventory;
        public final ModConfigSpec.BooleanValue flight;
        public final ModConfigSpec.DoubleValue reach;

        public Client(ModConfigSpec.Builder builder) {

            builder.comment("Cheat Mode Mod Configurations | NO RESTART REQUIRED")
                   .push(Reference.MOD_ID);

            instantCreativeInventory = builder
                    .comment("Instantly open the creative inventory (true) or open " +
                            "the survival inventory with a button to open the creative inventory (false)? (default: true)")
                    .define("instant_creative_inventory", true);

            reach = builder
                    .comment("Reach for placing/breaking blocks and attacking entites (default: 3.0)")
                    .defineInRange("reach", 3.0, 0.0, 200.0);

            flight = builder
                    .comment("Toggle Flight (default: false)")
                    .define("flight", false);

            builder.pop();
        }
    }

    public static final ModConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;
    static {
        final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }
}
