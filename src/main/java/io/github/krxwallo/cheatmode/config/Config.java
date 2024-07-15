package io.github.krxwallo.cheatmode.config;

import io.github.krxwallo.cheatmode.reference.Reference;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Author: Krxwallo
 */
public class Config {
    public static class Client {

        public final ModConfigSpec.BooleanValue instantCreativeInventory;
        public final ModConfigSpec.BooleanValue flight;
        public final ModConfigSpec.DoubleValue interactionReach;
        public final ModConfigSpec.DoubleValue blockReach;

        public Client(ModConfigSpec.Builder builder) {

            builder.comment("Cheat Mode Mod Configurations | NO RESTART REQUIRED")
                   .push(Reference.MOD_ID);

            instantCreativeInventory = builder
                    .comment("Instantly open the creative inventory (true) or open " +
                            "the survival inventory with a button to open the creative inventory (false)? (default: true)")
                    .define("instant_creative_inventory", true);

            var defaultInteractionReach = Attributes.ENTITY_INTERACTION_RANGE.value().getDefaultValue();
            var defaultBlockReach = Attributes.BLOCK_INTERACTION_RANGE.value().getDefaultValue();

            interactionReach = builder
                    .comment("Reach for interacting with/attacking entities (default: " + defaultInteractionReach + ")")
                    .defineInRange("interaction_reach", defaultInteractionReach, 0.0, 200.0);

            blockReach = builder
                    .comment("Reach for placing/breaking blocks (default: " + defaultBlockReach + ")")
                    .defineInRange("block_reach", defaultBlockReach, 0.0, 200.0);

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
