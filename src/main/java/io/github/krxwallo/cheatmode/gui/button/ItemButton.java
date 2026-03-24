package io.github.krxwallo.cheatmode.gui.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
public class ItemButton extends Button {
    public ItemButton(int xIn, int yIn, OnPress action) {
        super(xIn, yIn, 16, 16, Component.empty(), action, DEFAULT_NARRATION);
    }

    protected void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.alpha = 200.0F;
        graphics.renderItem(new ItemStack(Blocks.GRASS_BLOCK), getX(), getY());
        this.alpha = 1.0F;
    }
}
