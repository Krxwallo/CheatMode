package com.justAm0dd3r.cheatmode.gui.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

/**
 * Author: justAm0dd3r
 */
public class ItemButton extends Button {
    private final int x, y;

    public ItemButton(int xIn, int yIn, IPressable pressable) {
        super(xIn, yIn, 16, 16, new TranslationTextComponent(""), pressable);
        this.x = xIn;
        this.y = yIn;
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderButton();
        super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void renderButton() {
        RenderHelper.enableStandardItemLighting();
        ItemRenderer itemRender = Minecraft.getInstance().getItemRenderer();

        GL11.glTranslatef(0.0F, 0.0F, 32.0F);

        this.alpha = 200.0F;
        itemRender.zLevel = 200.0F;
        itemRender.renderItemAndEffectIntoGUI(new ItemStack(Blocks.GRASS_BLOCK), x, y);
        this.alpha = 0.0F;
        itemRender.zLevel = 0.0F;

        RenderHelper.disableStandardItemLighting();
    }
}
