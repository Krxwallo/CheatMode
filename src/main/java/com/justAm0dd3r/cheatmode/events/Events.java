package com.justAm0dd3r.cheatmode.events;

import com.justAm0dd3r.cheatmode.config.Config;
import com.justAm0dd3r.cheatmode.gui.button.ItemButton;
import com.justAm0dd3r.cheatmode.gui.button.ToggleButton;
import com.justAm0dd3r.cheatmode.gui.screen.CheatModeScreen;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class Events {
    // Everything here is bad practise, but as it is only intended for single player, I DONT CARE
    private static final Logger LOGGER = LogManager.getLogger();
    public GameType gameTypeBefore;
    public boolean screenOpen;
    private ItemButton button;
    private boolean firstTime = true;

    @SubscribeEvent
    public void onScreenInit(GuiScreenEvent.InitGuiEvent.Post event) {
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
        else if (event.getGui() instanceof CreativeScreen){
            // Creative Inventory
            // Add other buttons such as enable fly
            CreativeScreen gui = ((CreativeScreen) event.getGui());

            int j = gui.height / 4 + 48;

            event.addWidget(new ToggleButton(gui.width / 2 - 200, gui.height / 6 + 48 - 6, 98, 20,
                    "Double Speed", Config.COMMON.doubleSpeed.get(),
                    button -> {
                        Config.COMMON.doubleSpeed.set(((ToggleButton) button).getState());
                        Config.COMMON.doubleSpeed.save();
                        updateSpeed(getServerPlayer());
                    }));
        }
    }

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent evt) {
        if (evt.getEntity() instanceof PlayerEntity) updateSpeed(((PlayerEntity) evt.getEntity()));
    }

    private void updateSpeed(PlayerEntity player) {
        if (Config.COMMON.doubleSpeed.get()) {
            Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.2F);
            try {Objects.requireNonNull(player.getAttribute(Attributes.FLYING_SPEED)).setBaseValue(0.2F); } catch (Exception ignored){}
        }
        else {
            Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.1F);
            try {Objects.requireNonNull(player.getAttribute(Attributes.FLYING_SPEED)).setBaseValue(0.1F); } catch (Exception ignored){}
        }
    }

    @SubscribeEvent
    public void fovUpdate(FOVUpdateEvent evt) {
        float newFov = evt.getFov();
        if (Config.COMMON.doubleSpeed.get()) {
            newFov = (newFov/3)*2;
            evt.setNewfov(newFov);
        }
    }

    @SubscribeEvent
    public void onScreenDrawPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.getGui() instanceof InventoryScreen && button != null) {
            if(button.isMouseOver(event.getMouseX(), event.getMouseY())) {
                screenRenderToolTip(((InventoryScreen) event.getGui()), new TranslationTextComponent("gui.cheat_mode.open_creative_inventory"),
                        event.getMouseX(), event.getMouseY());
            }
        }
    }

    private void screenRenderToolTip(InventoryScreen screen, TranslationTextComponent name, int mouseX, int mouseY) {
        screen.renderTooltip(new MatrixStack(), name, mouseX, mouseY);
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

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof PlayerEntity) {
            PlayerEntity pEntity = ((PlayerEntity) entity);
            if (!pEntity.abilities.isCreativeMode) {
                LOGGER.debug("onBlockPlace() called with player entity.");
                ItemStack stack = pEntity.getHeldItemMainhand();
                stack.grow(1);

                pEntity.setHeldItem(Hand.MAIN_HAND, stack);
                //((PlayerEntity) event.getEntity()).addItemStackToInventory(new ItemStack(((PlayerEntity) event.getEntity()).getHeldItemMainhand().getItem().getItem(), 1));
                //pEntity.inventory.addItemStackToInventory(new ItemStack(pEntity.getHeldItemMainhand().getItem(),pEntity.getHeldItemMainhand().getCount() + 1));
            }
        }
    }
}