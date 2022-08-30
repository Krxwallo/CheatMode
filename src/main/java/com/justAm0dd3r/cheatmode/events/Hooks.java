package com.justAm0dd3r.cheatmode.events;

import com.justAm0dd3r.cheatmode.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import javax.annotation.Nullable;


public final class Hooks {

    private static GameType previousGameMode = GameType.SURVIVAL;
    public static Minecraft mc() { return Minecraft.getInstance(); }
    public static boolean singleplayer() { return mc().isLocalServer(); }

    @Nullable
    private static ServerPlayer serverPlayer() {
        if (mc().player == null) return null;
        if (mc().getSingleplayerServer() == null) return null;

        return mc().getSingleplayerServer().getPlayerList().getPlayer(mc().player.getUUID());
    }

    public static void onScreenInit(Screen screen) {
        if (mc().player == null) return;

        if (screen instanceof InventoryScreen) {
            if (Config.CLIENT.instantCreativeInventory.get()) {
                if (mc().gameMode == null) return;
                previousGameMode = mc().gameMode.getPlayerMode();

                if (singleplayer()) {
                    var player = serverPlayer();
                    if (player == null) return;

                    player.setGameMode(GameType.CREATIVE);
                }
                else {
                    // Server -> require op
                    if (!mc().player.hasPermissions(2)) mc().player.sendSystemMessage(Component.translatable("message.cheatmode.no_permissions").withStyle(ChatFormatting.RED));
                    else mc().player.commandUnsigned("gamemode creative");
                }
            }
            else {
                // TODO use ItemButton

            }
        }
    }

    public static void onScreenClose(Screen screen) {
        if (mc().player == null) return;

        if (screen instanceof CreativeModeInventoryScreen) {
            if (singleplayer()) {
                var player = serverPlayer();
                if (player == null) return;
                if (mc().gameMode == null || mc().gameMode.getPreviousPlayerMode() == null) return;

                player.setGameMode(previousGameMode);
            }
            else {
                // Server -> require op
                if (!mc().player.hasPermissions(2)) mc().player.sendSystemMessage(Component.translatable("message.cheatmode.no_permissions").withStyle(ChatFormatting.RED));
                else mc().player.commandUnsigned("gamemode " + previousGameMode.name().toLowerCase());
            }
        }
    }
}
