package com.atrik.betteraddits.guis;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.system.CallbackI;

public class BetterImageButton extends Button {
    private final ResourceLocation resourceLocation;
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffTex;
    private final int textureWidth;
    private final int textureHeight;
    private final int buttonTextColor;
    private final float textSize;
    private final String buttonText;
    public BetterImageButton(int posX, int posY, int sizeX, int sizeY, int xOffest, int yOffest, int hoverYOffest, ResourceLocation resourceLocation, int textureWidth, int textureHeight, Button.IPressable btnAction, Button.ITooltip btnTooltip, String text, String textClrHex,float textSize) {
        super(posX, posY, sizeX, sizeY, new StringTextComponent(""), btnAction, btnTooltip);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.xTexStart = xOffest;
        this.yTexStart = yOffest;
        this.yDiffTex = hoverYOffest;
        this.resourceLocation = resourceLocation;
        this.buttonText = text;
        this.buttonTextColor = Integer.parseInt(textClrHex.replace("#", ""), 16);
        this.textSize = textSize;
    }

    @Override
    public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(this.resourceLocation);
        int i = this.yTexStart;
        if (this.isHovered()) {
            i += this.yDiffTex;
        }

        RenderSystem.enableDepthTest();
        blit(p_230431_1_, this.x, this.y, (float)this.xTexStart, (float)i, this.width, this.height, this.textureWidth, this.textureHeight);
        p_230431_1_.scale(textSize, textSize, textSize);

        drawCenteredString(p_230431_1_, minecraft.font, this.buttonText, (int) ((this.x + this.width / 2) * (1 / textSize)), (int) ((this.y + (this.height - 8) / 2) * (1 / textSize)), buttonTextColor);
        p_230431_1_.scale((1 / textSize), (1 / textSize), (1 / textSize));
        if (this.isHovered()) {
            this.renderToolTip(p_230431_1_, p_230431_2_, p_230431_3_);
        }

    }
}
