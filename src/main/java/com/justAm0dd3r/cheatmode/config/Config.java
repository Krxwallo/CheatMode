package com.justAm0dd3r.cheatmode.config;

import com.justAm0dd3r.cheatmode.reference.Reference;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Author: justAm0dd3r
 */
public class Config {
    public static class Common {

        public final ForgeConfigSpec.BooleanValue instantCreativeInventory;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Cheat Mode Mod Configurations")
                   .push(Reference.MOD_ID);

            instantCreativeInventory = builder
                    .comment("Instantly open the creative inventory (true) or open " +
                            "the survival inventory with a button to open the creative inventory (false)? (default: true)")
                    .worldRestart()
                    .define("instant_creative_inventory", true);

            builder.pop();
        }
    }

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}
