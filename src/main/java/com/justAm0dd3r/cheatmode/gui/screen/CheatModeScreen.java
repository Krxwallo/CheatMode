package com.justAm0dd3r.cheatmode.gui.screen;

import com.justAm0dd3r.cheatmode.CheatMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.GameType;

public class CheatModeScreen extends CreativeScreen {

    public CheatModeScreen(PlayerEntity player, ServerPlayerEntity serverPlayerEntity) {
        super(player);
        assert Minecraft.getInstance().playerController != null;
        CheatMode.getEvents().gameTypeBefore = Minecraft.getInstance().playerController.getCurrentGameType();
        CheatMode.getEvents().screenOpen = true;
        serverPlayerEntity.setGameType(GameType.CREATIVE);
    }
}
