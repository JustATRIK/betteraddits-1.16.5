package com.atrik.betteraddits.utils;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;

public class BetterItemRenderer extends ItemRenderer {

    public static final ResourceLocation ENCHANT_GLINT_LOCATION = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private static final Set<Item> IGNORED = Sets.newHashSet(Items.AIR);
    public float blitOffset;
    private final ItemModelMesher itemModelShaper;
    private final TextureManager textureManager;
    private final ItemColors itemColors;

    public BetterItemRenderer(TextureManager p_i46552_1_, ModelManager p_i46552_2_, ItemColors p_i46552_3_) {
        super(p_i46552_1_, p_i46552_2_, p_i46552_3_);
        this.textureManager = p_i46552_1_;
        this.itemModelShaper = new net.minecraftforge.client.ItemModelMesherForge(p_i46552_2_);

        for(Item item : Registry.ITEM) {
            if (!IGNORED.contains(item)) {
                this.itemModelShaper.register(item, new ModelResourceLocation(Registry.ITEM.getKey(item), "inventory"));
            }
        }

        this.itemColors = p_i46552_3_;
    }
    protected void renderGuiItem(ItemStack p_191962_1_, int p_191962_2_, int p_191962_3_, IBakedModel p_191962_4_, int sizeX, int sizeY) {
        RenderSystem.pushMatrix();
        this.textureManager.bind(AtlasTexture.LOCATION_BLOCKS);
        this.textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.translatef((float)p_191962_2_, (float)p_191962_3_, 100.0F + this.blitOffset);
        RenderSystem.translatef(8.0F, 8.0F, 0.0F);
        RenderSystem.scalef(1.0F, -1.0F, 1.0F);
        RenderSystem.scalef(sizeX, sizeY, sizeY);
        MatrixStack matrixstack = new MatrixStack();
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean flag = !p_191962_4_.usesBlockLight();
        if (flag) {
            RenderHelper.setupForFlatItems();
        }

        this.render(p_191962_1_, ItemCameraTransforms.TransformType.GUI, false, matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, p_191962_4_);
        irendertypebuffer$impl.endBatch();
        RenderSystem.enableDepthTest();
        if (flag) {
            RenderHelper.setupFor3DItems();
        }

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
    }

    @Override
    public void renderAndDecorateItem(ItemStack p_180450_1_, int p_180450_2_, int p_180450_3_) {
        this.tryRenderGuiItem(Minecraft.getInstance().player, p_180450_1_, p_180450_2_, p_180450_3_, 16, 16);
    }
    public void renderAndDecorateItem(ItemStack p_180450_1_, int p_180450_2_, int p_180450_3_, int sizeX, int sizeY) {
        this.tryRenderGuiItem(Minecraft.getInstance().player, p_180450_1_, p_180450_2_, p_180450_3_, sizeX, sizeY);
    }
    private void tryRenderGuiItem(@Nullable LivingEntity p_239387_1_, ItemStack p_239387_2_, int p_239387_3_, int p_239387_4_, int sizeX, int sizeY) {
        if (!p_239387_2_.isEmpty()) {
            this.blitOffset += 50.0F;

            try {
                this.renderGuiItem(p_239387_2_, p_239387_3_, p_239387_4_, this.getModel(p_239387_2_, (World)null, p_239387_1_), sizeX, sizeY);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Item being rendered");
                crashreportcategory.setDetail("Item Type", () -> {
                    return String.valueOf((Object)p_239387_2_.getItem());
                });
                crashreportcategory.setDetail("Registry Name", () -> String.valueOf(p_239387_2_.getItem().getRegistryName()));
                crashreportcategory.setDetail("Item Damage", () -> {
                    return String.valueOf(p_239387_2_.getDamageValue());
                });
                crashreportcategory.setDetail("Item NBT", () -> {
                    return String.valueOf((Object)p_239387_2_.getTag());
                });
                crashreportcategory.setDetail("Item Foil", () -> {
                    return String.valueOf(p_239387_2_.hasFoil());
                });
                throw new ReportedException(crashreport);
            }

            this.blitOffset -= 50.0F;
        }
    }
}
