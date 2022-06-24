package com.justAm0dd3r.cheatmode.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.TextComponent;

public class DayButton extends Button {
    private final Minecraft minecraft = Minecraft.getInstance();

    public DayButton(int x, int y, int width, int height, String message) {
        super(x, y, width, height, new TextComponent(message), b -> {});
    }

    @Override
    public void onPress() {
        if (minecraft.player == null) return;
        if (!minecraft.hasSingleplayerServer()) return;
        IntegratedServer server = minecraft.getSingleplayerServer();
        if (server == null) return;
        server.overworld().setDayTime(1000);
    }
}
