package com.justAm0dd3r.cheatmode.mixins;

import com.justAm0dd3r.cheatmode.options.CheatModeOptions;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.justAm0dd3r.cheatmode.events.Hooks.mc;


@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Inject(at = @At("TAIL"), method = "init()V")
    public void init(CallbackInfo ci) {
        var screen = (ChatScreen) (Object) this;
        if (screen instanceof InBedChatScreen) return;

        var width = screen.width;
        var height = screen.height;

        int i = 0;
        for(OptionInstance<?> option : new OptionInstance[]{CheatModeOptions.instantCreativeInventory, CheatModeOptions.flight, CheatModeOptions.reach}) {
            int j = width / 5 * 3;
            int k = height / 10 - 12 + 24 * i;
            var button = option.createButton(mc().options, j, k, 150);
            screen.renderables.add(button);
            cheatMode$addWidget(screen, button);
            ++i;
        }
    }

    @Unique
    private static <T extends GuiEventListener & NarratableEntry> void cheatMode$addWidget(Screen screen, T button) {
        var screenAccessor = (ScreenAccessor) screen;

        screenAccessor.getRenderables().add((Renderable) button);
        screenAccessor.getNarratables().add(button);
        screenAccessor.getChildren().add(button);
    }
}