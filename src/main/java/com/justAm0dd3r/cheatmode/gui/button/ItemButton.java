package com.justAm0dd3r.cheatmode.gui.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

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
    public void func_230431_b_(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderButton();
        super.func_230431_b_(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void renderButton() {
        RenderHelper.enableStandardItemLighting();
        ItemRenderer itemRender = Minecraft.getInstance().getItemRenderer();

        RenderSystem.translatef(0.0F, 0.0F, 32.0F);
        this.field_230695_q_ = 200.0F;
        itemRender.zLevel = 200.0F;
        itemRender.renderItemAndEffectIntoGUI(new ItemStack(Blocks.GRASS_BLOCK), x, y);
        this.field_230695_q_ = 0.0F;
        itemRender.zLevel = 0.0F;

        RenderHelper.disableStandardItemLighting();
    }
}
