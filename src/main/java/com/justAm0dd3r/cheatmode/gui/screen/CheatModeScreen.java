package com.justAm0dd3r.cheatmode.gui.screen;

import com.justAm0dd3r.cheatmode.CheatMode;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class CheatModeScreen extends CreativeModeInventoryScreen {
    public CheatModeScreen(Player player, ServerPlayer serverPlayer, boolean setGameType) {
        super(player);
        if (setGameType) {
            CheatMode.getEvents().gameTypeBefore = serverPlayer.gameMode.getGameModeForPlayer();
            CheatMode.getEvents().screenOpen = true;
            serverPlayer.setGameMode(GameType.CREATIVE);
        }
    }
}
