package com.justAm0dd3r.cheatmode.events;

import com.justAm0dd3r.cheatmode.CheatMode;
import com.justAm0dd3r.cheatmode.config.Config;
import com.justAm0dd3r.cheatmode.gui.button.ItemButton;
import com.justAm0dd3r.cheatmode.gui.screen.CheatModeScreen;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class Events {
    private static final Logger LOGGER = LogManager.getLogger();
    public GameType gameTypeBefore;
    public boolean screenOpen;
    private ItemButton button;
    private boolean firstTime = true;


    @SubscribeEvent
    public void onScreenInit(GuiScreenEvent.InitGuiEvent event) {
        if (event.getGui() instanceof InventoryScreen) {
            assert Minecraft.getInstance().playerController != null;
            if (Minecraft.getInstance().playerController.getCurrentGameType() != GameType.CREATIVE) {

                if (Config.COMMON.instantCreativeInventory.get()) {
                    // Directly open Creative Inventory

                    if (firstTime) {
                        firstTime = false;
                        if (Minecraft.getInstance().player == null) {
                            LOGGER.error("Player is null");
                            return;
                        }

                        assert Minecraft.getInstance().playerController != null;
                        this.gameTypeBefore = Minecraft.getInstance().playerController.getCurrentGameType();
                        this.screenOpen = true;
                        Objects.requireNonNull(getServerPlayer()).setGameType(GameType.CREATIVE);

                        Minecraft.getInstance().displayGuiScreen(
                                new CheatModeScreen(Minecraft.getInstance().player, Objects.requireNonNull(getServerPlayer()), false));
                    }
                }
                else {
                    // Add a button

                    int guiCenterX = ((InventoryScreen) event.getGui()).getGuiLeft();
                    int guiCenterY = ((InventoryScreen) event.getGui()).getGuiTop();

                    if (Minecraft.getInstance().player == null) {
                        LOGGER.error("Player is null");
                        return;
                    }

                    event.addWidget(this.button = new ItemButton(guiCenterX + 77, guiCenterY + 30,
                            button -> Minecraft.getInstance().displayGuiScreen(new CheatModeScreen(Minecraft.getInstance().player,
                                    Objects.requireNonNull(getServerPlayer()), true))));
                }
            }
        }
    }

    @SubscribeEvent
    public void onScreenDrawPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.getGui() instanceof InventoryScreen && button != null) {
            if(button.func_231047_b_(event.getMouseX(), event.getMouseY())) {
                screenRenderToolTip(((InventoryScreen) event.getGui()), new TranslationTextComponent("gui.cheat_mode.open_creative_inventory"),
                        event.getMouseX(), event.getMouseY());
            }
        }
    }

    private void screenRenderToolTip(InventoryScreen screen, TranslationTextComponent name, int mouseX, int mouseY) {
        screen.func_238652_a_(new MatrixStack(), ITextProperties.func_240652_a_(name.getString()), mouseX, mouseY);
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event) {
        if (screenOpen && event.getGui() == null) {
            screenOpen = false;
            firstTime = true;
            ServerPlayerEntity serverPlayerEntity = getServerPlayer();
            assert serverPlayerEntity != null;
            serverPlayerEntity.setGameType(gameTypeBefore);
        }
    }

    @Nullable
    private ServerPlayerEntity getServerPlayer() {
        List<ServerPlayerEntity> players = Objects.requireNonNull(Minecraft.getInstance().getIntegratedServer()).getPlayerList().getPlayers();

        assert Minecraft.getInstance().player != null;
        for (ServerPlayerEntity player : players) {
            if (player.getName().getString().equals(Minecraft.getInstance().player.getName().getString())) {
                return player;
            }
        }
        return null;
    }
}