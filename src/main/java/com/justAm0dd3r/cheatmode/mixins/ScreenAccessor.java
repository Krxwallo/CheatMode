package com.justAm0dd3r.cheatmode.mixins;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Screen.class)
public interface ScreenAccessor {
    @Accessor
    List<NarratableEntry> getNarratables();
    @Accessor
    List<Renderable> getRenderables();
    @Accessor
    List<GuiEventListener> getChildren();
}
