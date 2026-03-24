package io.github.krxwallo.cheatmode.events;

import io.github.krxwallo.cheatmode.config.Config;
import io.github.krxwallo.cheatmode.gui.button.ItemButton;
import io.github.krxwallo.cheatmode.options.CheatModeOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;
import net.neoforged.neoforge.client.event.ClientPlayerChangeGameTypeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.GameShuttingDownEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@EventBusSubscriber(value = Dist.CLIENT)
public final class Hooks {
    private static ItemButton button;
    private static boolean pendingCreativeScreen;
    private static final Map<ChatScreen, List<AbstractWidget>> chatOptionWidgets = new WeakHashMap<>();

    private static GameType previousGameMode = GameType.SURVIVAL;
    public static boolean open = false; // "cheat mode screen" open?
    public static Minecraft mc() { return Minecraft.getInstance(); }
    public static boolean singleplayer() { return mc().isLocalServer(); }

    @Nullable
    private static ServerPlayer serverPlayer() {
        if (mc().player == null) return null;
        if (mc().getSingleplayerServer() == null) return null;

        return mc().getSingleplayerServer().getPlayerList().getPlayer(mc().player.getUUID());
    }

    private static void creative() {
        if (mc().player == null) return;
        if (mc().gameMode == null) return;
        if (mc().gameMode.getPlayerMode().isCreative()) return;

        previousGameMode = mc().gameMode.getPlayerMode();
        pendingCreativeScreen = mc().screen instanceof InventoryScreen;

        if (singleplayer()) {
            var player = serverPlayer();
            if (player == null) return;

            player.setGameMode(GameType.CREATIVE);
        }
        else {
            mc().player.connection.sendCommand("gamemode creative");
        }
    }

    public static void onScreenInit(Screen screen) {
        if (screen instanceof InventoryScreen invScreen) {
            if (Config.CLIENT.instantCreativeInventory.get()) creative();
            else {
                button = new ItemButton(invScreen.getGuiLeft() + 77, invScreen.getGuiTop() + 30, b -> creative());
                button.setTooltip(Tooltip.create(Component.translatable("gui.cheatmode.open_creative_inventory").withStyle(ChatFormatting.GOLD)));
                addButton(invScreen, button);
            }
        }
    }

    @SubscribeEvent
    public static void onScreenInitPost(ScreenEvent.Init.Post event) {
        onScreenInit(event.getScreen());

        if (event.getScreen() instanceof ChatScreen screen) {
            initChatScreen(screen);
        }
    }

    private static void initChatScreen(ChatScreen screen) {
        if (screen instanceof InBedChatScreen) return;
        chatOptionWidgets.remove(screen);

        int width = screen.width;
        int height = screen.height;
        List<AbstractWidget> widgets = new ArrayList<>();

        int i = 0;
        for (OptionInstance<?> option : new OptionInstance[]{CheatModeOptions.instantCreativeInventory, CheatModeOptions.flight, CheatModeOptions.blockReach, CheatModeOptions.interactionReach}) {
            int j = width / 5 * 3;
            int k = height / 10 - 12 + 24 * i;
            var optionButton = option.createButton(mc().options, j, k, 150);
            widgets.add(optionButton);
            screen.renderables.add(optionButton);
            addWidget(screen, optionButton);
            ++i;
        }

        chatOptionWidgets.put(screen, widgets);
    }

    private static void addButton(Screen screen, Button button) {
        screen.renderables.add(button);
        //noinspection unchecked
        ((java.util.List<GuiEventListener>) screen.children()).add(button);
    }

    private static <T extends GuiEventListener & NarratableEntry> void addWidget(Screen screen, T widget) {
        //noinspection unchecked
        ((java.util.List<GuiEventListener>) screen.children()).add(widget);

        try {
            var field = Screen.class.getDeclaredField("narratables");
            field.setAccessible(true);
            //noinspection unchecked
            ((java.util.List<NarratableEntry>) field.get(screen)).add(widget);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void onScreenClose(Screen screen) {
        if (mc().player == null) return;

        if (screen instanceof CreativeModeInventoryScreen) {
            if (!open) return;
            open = false;

            if (singleplayer()) {
                var player = serverPlayer();
                if (player == null) return;
                if (mc().gameMode == null || mc().gameMode.getPreviousPlayerMode() == null) return;

                player.setGameMode(previousGameMode);
            }
            else {
                mc().player.connection.sendCommand("gamemode " + previousGameMode.name().toLowerCase());
            }
        }
    }

    @SubscribeEvent
    public static void onScreenClosing(ScreenEvent.Closing event) {
        onScreenClose(event.getScreen());
    }

    @SubscribeEvent
    public static void onGameShuttingDown(GameShuttingDownEvent event) {
        if (mc().screen != null) {
            onScreenClose(mc().screen);
        }
    }

    @SubscribeEvent
    public static void onSystemChat(ClientChatReceivedEvent.System event) {
        var msg = event.getMessage();
        if (msg.getContents() instanceof TranslatableContents contents) {
            if (!Hooks.singleplayer() && contents.getKey().equalsIgnoreCase("commands.gamemode.success.self")) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onClientPlayerChangeGameType(ClientPlayerChangeGameTypeEvent event) {
        if (!event.getNewGameType().isCreative() || mc().player == null) return;
        if (!pendingCreativeScreen || !(mc().screen instanceof InventoryScreen)) return;

        open = true;
        pendingCreativeScreen = false;
        mc().setScreen(new CreativeModeInventoryScreen(mc().player, mc().player.connection.enabledFeatures(), mc().options.operatorItemsTab().get()));
    }

    @SubscribeEvent
    public static void onChatKeyPressed(ScreenEvent.KeyPressed.Pre event) {
        if (!(event.getScreen() instanceof ChatScreen screen)) return;

        int keyCode = event.getKeyCode();
        if (keyCode != 264 && keyCode != 265) return;

        List<AbstractWidget> widgets = chatOptionWidgets.get(screen);
        if (widgets == null || widgets.isEmpty()) return;

        if (widgets.contains(screen.getFocused())) {
            screen.setFocused(null);
        }

        // Keep chat's history behavior, but temporarily remove the injected widgets from focus navigation.
        //noinspection unchecked
        ((List<GuiEventListener>) screen.children()).removeAll(widgets);

        try {
            var narratablesField = Screen.class.getDeclaredField("narratables");
            narratablesField.setAccessible(true);
            //noinspection unchecked
            ((List<NarratableEntry>) narratablesField.get(screen)).removeAll(widgets);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        screen.moveInHistory(keyCode == 265 ? -1 : 1);

        //noinspection unchecked
        ((List<GuiEventListener>) screen.children()).addAll(widgets);
        try {
            var narratablesField = Screen.class.getDeclaredField("narratables");
            narratablesField.setAccessible(true);
            //noinspection unchecked
            ((List<NarratableEntry>) narratablesField.get(screen)).addAll(widgets);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide() && !singleplayer()) return;

        updateFlight(player);
        updateReach(player);
    }

    private static void updateFlight(Player player) {
        boolean mayFly = (mc().gameMode != null && mc().gameMode.getPlayerMode().isCreative()) || Config.CLIENT.flight.get();
        if (player.getAbilities().mayfly != mayFly) {
            player.getAbilities().mayfly = mayFly;
            if (!mayFly) {
                player.getAbilities().flying = false;
            }

            if (player instanceof net.minecraft.client.player.LocalPlayer localPlayer) {
                localPlayer.onUpdateAbilities();
            }
        }
    }

    private static void updateReach(Player player) {
        var attributes = player.getAttributes();
        var blockReach = attributes.getInstance(Attributes.BLOCK_INTERACTION_RANGE);
        if (blockReach != null) {
            blockReach.setBaseValue(Config.CLIENT.blockReach.get());
        }

        var entityReach = attributes.getInstance(Attributes.ENTITY_INTERACTION_RANGE);
        if (entityReach != null) {
            entityReach.setBaseValue(Config.CLIENT.interactionReach.get());
        }
    }
}
