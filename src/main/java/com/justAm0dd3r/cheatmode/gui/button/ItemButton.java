package com.justAm0dd3r.cheatmode.gui.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class ItemButton extends Button {
    public ItemButton(int xIn, int yIn, OnPress action) {
        super(xIn, yIn, 16, 16, MutableComponent.create(ComponentContents.EMPTY), action, DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderButton(graphics);
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
    }

    public void renderButton(GuiGraphics graphics) {
        this.alpha = 200.0F;
        graphics.renderItem(new ItemStack(Blocks.GRASS_BLOCK), getX(), getY());
        this.alpha = 0.0F;
    }
}
