package com.justAm0dd3r.cheatmode.events;

import com.justAm0dd3r.cheatmode.config.Config;
import com.justAm0dd3r.cheatmode.gui.button.CommandButton;
import com.justAm0dd3r.cheatmode.gui.button.ItemButton;
import com.justAm0dd3r.cheatmode.gui.button.ToggleButton;
import com.justAm0dd3r.cheatmode.gui.screen.CheatModeScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

// Everything here is bad practise, but as it is only intended for single player, I DON'T CARE
public class Events {
    public GameType gameTypeBefore;
    public boolean screenOpen;
    private ItemButton button;
    private boolean firstTime = true;
    private final Minecraft minecraft = Minecraft.getInstance();

    @Nullable
    private ServerPlayer getServerPlayer() {
        if (!minecraft.hasSingleplayerServer() || minecraft.player == null) return null;
        IntegratedServer server = minecraft.getSingleplayerServer();
        assert server != null;

        PlayerList players = server.getPlayerList();

        return players.getPlayer(minecraft.player.getUUID());
    }

    @SubscribeEvent
    public void onScreenInit(GuiScreenEvent.InitGuiEvent.Post event) {
        ServerPlayer player = getServerPlayer();
        if (player == null | minecraft.player == null) return;

        if (event.getGui() instanceof InventoryScreen) {
            assert Minecraft.getInstance().gameMode != null;
            if (player.gameMode.getGameModeForPlayer() != GameType.CREATIVE) {

                if (Config.COMMON.instantCreativeInventory.get()) {
                    // Directly open Creative Inventory

                    if (firstTime) {
                        firstTime = false;

                        this.gameTypeBefore = player.gameMode.getGameModeForPlayer();
                        this.screenOpen = true;
                        player.setGameMode(GameType.CREATIVE);

                        minecraft.pushGuiLayer(new CheatModeScreen(minecraft.player, player, false));
                    }
                }
                else {
                    // Add a button

                    int guiCenterX = ((InventoryScreen) event.getGui()).getGuiLeft();
                    int guiCenterY = ((InventoryScreen) event.getGui()).getGuiTop();

                    event.addWidget(this.button = new ItemButton(guiCenterX + 77, guiCenterY + 30,
                            button -> minecraft.pushGuiLayer(new CheatModeScreen(minecraft.player, player, true))));
                }
            }
        }
        else if (event.getGui() instanceof CreativeModeInventoryScreen gui){
            // Creative Inventory
            // Add other buttons such as enable fly

            /*event.addWidget(new ToggleButton(gui.width / 2 - 200, gui.height / 6 + 48 - 6, 98, 20,
                    "Double Speed", Config.COMMON.doubleSpeed.get(),
                    button -> {
                        Config.COMMON.doubleSpeed.set(((ToggleButton) button).getState());
                        Config.COMMON.doubleSpeed.save();
                        if (minecraft.player == null) return;
                        updateSpeed(minecraft.player);
                    }));*/
            event.addWidget(new ToggleButton(gui.width / 2 - 200, gui.height / 6 + 48 - 6 /*- 24*/, 98, 20,
                    "InstantOpen", Config.COMMON.instantCreativeInventory.get(),
                    button -> {
                        Config.COMMON.instantCreativeInventory.set(((ToggleButton) button).getState());
                        Config.COMMON.instantCreativeInventory.save();
                    }));
            event.addWidget(new CommandButton(gui.width / 2 - 200, gui.height / 6 + 48 - 6 + 24, 98, 20,
                    "Set Day", "/time set day"));
        }
    }

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent evt) {
        if (evt.getEntity() instanceof Player player) updateSpeed(player);

    }

    private void updateSpeed(Player player) {
/*
        if (Config.COMMON.doubleSpeed.get()) {
            AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (movementSpeed != null) movementSpeed.setBaseValue(1);

            AttributeInstance flyingSpeed = player.getAttribute(Attributes.FLYING_SPEED);
            if (flyingSpeed != null) flyingSpeed.setBaseValue(1);
        }
        else {
            AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (movementSpeed != null) movementSpeed.setBaseValue(0.1);
            AttributeInstance flyingSpeed = player.getAttribute(Attributes.FLYING_SPEED);
            if (flyingSpeed != null) flyingSpeed.setBaseValue(0.1);
        }*/
    }

/*    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent evt) {
        if (Config.COMMON.doubleSpeed.get()) {
            //evt.player.moveTo(evt.player.position().add(evt.player.getDeltaMovement().multiply(2, 2, 2)));
            //evt.player.move(MoverType.PLAYER, evt.player.getForward().multiply(2, 2, 2));

            //Vec3 movement = evt.player.getDeltaMovement().add(evt.player.setSpeed(););
            //evt.player.move(MoverType.SELF, movement);

            //evt.player.setDeltaMovement(evt.player.getDeltaMovement().multiply(2, 2, 2));

            //evt.player.setSpeed(2);
        }
        //else evt.player.setSpeed(0.1f);
    }*/

    /*@SubscribeEvent
    public void fovUpdate(FOVUpdateEvent evt) {
        float newFov = evt.getFov();
        if (Config.COMMON.doubleSpeed.get()) {
            newFov = (newFov / 3) * 2;
            evt.setNewfov(newFov);
        }
    }*/

    @SubscribeEvent
    public void onScreenDrawPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.getGui() instanceof InventoryScreen && button != null) {
            if(button.isMouseOver(event.getMouseX(), event.getMouseY())) {
                screenRenderToolTip(((InventoryScreen) event.getGui()), new TranslatableComponent("gui.cheat_mode.open_creative_inventory"),
                        event.getMouseX(), event.getMouseY());
            }
        }
    }

    private void screenRenderToolTip(InventoryScreen screen, TranslatableComponent name, int mouseX, int mouseY) {
        screen.renderTooltip(new PoseStack(), name, mouseX, mouseY);
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event) {
        if (screenOpen && event.getGui() == null) {
            screenOpen = false;
            firstTime = true;
            ServerPlayer player = getServerPlayer();
            if (player == null) return;
            player.setGameMode(gameTypeBefore);
        }
    }
/*
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
    */
}