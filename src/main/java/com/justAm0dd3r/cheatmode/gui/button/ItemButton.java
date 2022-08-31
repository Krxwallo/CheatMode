package com.justAm0dd3r.cheatmode.gui.button;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class ItemButton extends Button {
    private final int x, y;

    public ItemButton(int xIn, int yIn, OnPress action) {
        super(xIn, yIn, 16, 16, MutableComponent.create(ComponentContents.EMPTY), action);
        this.x = xIn;
        this.y = yIn;
    }

    @Override
    public void renderButton(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderButton();
        super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void renderButton() {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        this.alpha = 200.0F;
        renderer.blitOffset = 200.0F;
        renderer.renderAndDecorateItem(new ItemStack(Blocks.GRASS_BLOCK), x, y);
        this.alpha = 0.0F;
        renderer.blitOffset = 0.0F;
    }
}
