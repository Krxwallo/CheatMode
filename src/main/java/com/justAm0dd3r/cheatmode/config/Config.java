package com.justAm0dd3r.cheatmode.config;

import com.justAm0dd3r.cheatmode.reference.Reference;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Author: Krxwallo
 */
public class Config {
    public static class Client {

        public final ForgeConfigSpec.BooleanValue instantCreativeInventory;
        public final ForgeConfigSpec.BooleanValue flight;
        public final ForgeConfigSpec.DoubleValue reach;

        public Client(ForgeConfigSpec.Builder builder) {

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

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;
    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }
}
