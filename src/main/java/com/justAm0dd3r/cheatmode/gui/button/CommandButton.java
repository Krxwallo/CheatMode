package com.justAm0dd3r.cheatmode.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.TextComponent;

public class CommandButton extends Button {
    private final String command;
    private final Minecraft minecraft = Minecraft.getInstance();

    public CommandButton(int x, int y, int width, int height, String message, String command) {
        super(x, y, width, height, new TextComponent(message), b -> {});
        this.command = command;
    }

    @Override
    public void onPress() {
        if (minecraft.player == null) return;
        enableCheats();
        minecraft.player.chat(command);
    }

    private void enableCheats() {
        if (!minecraft.hasSingleplayerServer()) return;
        IntegratedServer server = minecraft.getSingleplayerServer();
        assert server != null;
        server.getPlayerList().setAllowCheatsForAllPlayers(true);
        if (minecraft.player == null) return;
        minecraft.player.setPermissionLevel(4);
    }
}
