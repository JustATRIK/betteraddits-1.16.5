package com.atrik.betteraddits.guis;

import com.atrik.betteraddits.BetterAddits;
import com.atrik.betteraddits.blocks.entities.PlayersShopBlockEntity;
import com.atrik.betteraddits.items.ItemsRegister;
import com.atrik.betteraddits.utils.BetterItemRenderer;
import com.atrik.betteraddits.utils.ServerMessagePackets.BuyBtnMessagePacket;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.ITickList;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.GameData;
import com.atrik.betteraddits.utils.PacketHandler;
import org.lwjgl.system.CallbackI;

import java.lang.reflect.Array;

import static com.atrik.betteraddits.utils.ItemUtils.getItemByStrID;
import static com.atrik.betteraddits.utils.PacketHandler.INSTANCE;

public class PlayersShopBlockScreen extends ContainerScreen<PlayersShopBlockGui>{
    public static final ResourceLocation TEXTURE = new ResourceLocation(BetterAddits.MOD_ID, "textures/gui/players_shop_block_gui.png");
    public String hexColorName = "#8fce00";
    private int invDisplayNameColor = Integer.parseInt(hexColorName.replace("#", ""), 16);
    public short quantity = 1;
    ItemStack sellItemStack = new ItemStack(Items.AIR);
    public BetterItemRenderer betterItemRenderer;
    private String inStockColor = "#8fce00";


    @Override
    public final void init() {
        super.init();
        this.addButton(new BetterImageButton(this.width / 2 - this.imageWidth / 2, this.height / 2 - 55, 20, 20, 83, 187, 20, TEXTURE, 256, 256, (a) -> {
            addOrMinusQuantity(-1, inventory.player.level);
        }, Button.NO_TOOLTIP, new TranslationTextComponent("betteraddits.container.playersShop.minus1BtnName").getString(), "#ff0000", 0.8f));
        this.addButton(new BetterImageButton(this.width / 2 - this.imageWidth / 2 + 22, this.height / 2 - 55, 20, 20, 83, 187, 20, TEXTURE, 256, 256, (a) -> {
            addOrMinusQuantity(-16, inventory.player.level);
        }, Button.NO_TOOLTIP, new TranslationTextComponent("betteraddits.container.playersShop.minus16BtnName").getString(), "#ff0000", 0.8f));
        this.addButton(new BetterImageButton(this.width / 2 - this.imageWidth / 2 + 44, this.height / 2 - 55, 20, 20, 83, 187, 20, TEXTURE, 256, 256, (a) -> {
            addOrMinusQuantity(-32, inventory.player.level);
        }, Button.NO_TOOLTIP, new TranslationTextComponent("betteraddits.container.playersShop.minus32BtnName").getString(), "#ff0000", 0.8f));
        this.addButton(new BetterImageButton(this.width / 2 - this.imageWidth / 2 + 66 + 36, this.height / 2 - 55, 20, 20, 83, 187, 20, TEXTURE, 256, 256, (a) -> {
            addOrMinusQuantity(1, inventory.player.level);
        }, Button.NO_TOOLTIP, new TranslationTextComponent("betteraddits.container.playersShop.plus1BtnName").getString(), "#8fce00", 0.8f));
        this.addButton(new BetterImageButton(this.width / 2 - this.imageWidth / 2 + 88 + 36, this.height / 2 - 55, 20, 20, 83, 187, 20, TEXTURE, 256, 256, (a) -> {
            addOrMinusQuantity(16, inventory.player.level);
        }, Button.NO_TOOLTIP, new TranslationTextComponent("betteraddits.container.playersShop.plus16BtnName").getString(), "#8fce00", 0.8f));
        this.addButton(new BetterImageButton(this.width / 2 - this.imageWidth / 2 + 110 + 36, this.height / 2 - 55, 20, 20, 83, 187, 20, TEXTURE, 256, 256, (a) -> {
            addOrMinusQuantity(32, inventory.player.level);
        }, Button.NO_TOOLTIP, new TranslationTextComponent("betteraddits.container.playersShop.plus32BtnName").getString(), "#8fce00", 0.8f));

        this.addButton(new BetterImageButton(this.width / 2 + this.imageWidth / 2 - 92, this.height / 2 - 20, 82, 22, 1, 187, 22, TEXTURE, 256, 256, (a) -> {
            INSTANCE.sendToServer(new BuyBtnMessagePacket(quantity, menu.tileEntity.getBlockPos()));
        }, Button.NO_TOOLTIP, new TranslationTextComponent("betteraddits.container.playersShop.buyBtnName").getString(), "#8fce00", 1f));
        this.addButton(new BetterImageButton(this.width / 2 - this.imageWidth / 2, this.height / 2 - 20, 82, 22, 1, 187, 22, TEXTURE, 256, 256, (a) -> {
            cancelButtonPressed();
        }, Button.NO_TOOLTIP, new TranslationTextComponent("betteraddits.container.playersShop.cancelBtnName").getString(), "#ff0000", 1f));
        betterItemRenderer = new BetterItemRenderer(minecraft.getTextureManager(), minecraft.getModelManager(), minecraft.getItemColors());
    }
    public PlayersShopBlockScreen(PlayersShopBlockGui container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int p_230451_2_, int p_230451_3_) {
        this.font.draw(matrixStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, this.invDisplayNameColor);
        this.font.draw(matrixStack, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float partialTicks) {
        this.renderBackground(matrixStack);
        this.inventoryLabelY = 88;
        this.inventoryLabelX = 5;
        this.titleLabelY = 2;
        //this.getMenu().getInventory().setItem(0, new ItemStack(getItemByStrID(menu.nbtData.getString("sellItemID")), this.quantity));
        super.render(matrixStack, x, y, partialTicks);
        this.renderTooltip(matrixStack, x, y);
        betterItemRenderer.renderAndDecorateItem(sellItemStack, (this.width - this.imageWidth) / 2 + menu.getSlot(0).x, (this.height - this.imageHeight) / 2 + menu.getSlot(0).y, 26, 26);
        betterItemRenderer.renderGuiItemDecorations(minecraft.font, sellItemStack, (this.width - this.imageWidth) / 2 + menu.getSlot(0).x, (this.height - this.imageHeight) / 2 + menu.getSlot(0).y);
        drawString(matrixStack, minecraft.font, new TranslationTextComponent("betteraddits.container.playersShop.inStockMsg").getString() + menu.tileEntity.inStock, (this.width / 2 - this.imageWidth / 2 + 44) + 130, this.height / 2 - 55, Integer.parseInt(this.inStockColor.replace("#", ""), 16));
    }


    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        if (minecraft == null) {
            return;
        }

        RenderSystem.color4f(1, 1, 1, 1);
        minecraft.getTextureManager().bind(TEXTURE);

        int posX = (this.width - 186) / 2;
        int posY = (this.height - 176) / 2;

        blit(matrixStack, posX, posY, 1, 1, 176, 186);
}

    public void cancelButtonPressed(){
        inventory.player.closeContainer();
    }

    public void addOrMinusQuantity(int quantity, World world){
        if (this.quantity + quantity >= 1) {
            this.quantity += quantity;
        }
        if (menu.nbtData.getInt("inStock") < this.quantity){
            this.inStockColor = "#ff0000";
        }
        else if (menu.nbtData.getInt("inStock") - this.quantity < this.quantity / 100 * 5){
            this.inStockColor = "#ffd500";
        }
        else {
            this.inStockColor = "#8fce00";
        }
        this.getMenu().getInventory().setItem(0, new ItemStack(getItemByStrID(menu.nbtData.getString("sellItemID")), this.quantity));
        System.out.println(menu.getTileEntity().serializeNBT());
        sellItemStack = new ItemStack(getItemByStrID(menu.nbtData.getString("sellItemID")), this.quantity);
    }
    public static void buyButtonPressed(PlayerInventory inventory, PlayersShopBlockEntity tileEntity, int quantity){
        if (tileEntity.inStock >= quantity) {
            tileEntity.inStock -= quantity;
            ItemStack itemStack = new ItemStack(getItemByStrID(tileEntity.sellItemID));
            int oldItemsCount = inventory.countItem(getItemByStrID(tileEntity.sellItemID));
            inventory.player.addItem(itemStack);
            int newItemsCount = inventory.countItem(getItemByStrID(tileEntity.sellItemID));
            itemStack.setCount(quantity - (newItemsCount - oldItemsCount));
            ItemEntity itemEntity = inventory.player.drop(itemStack, false);
            if (itemEntity != null) {
                itemEntity.setOwner(inventory.player.getUUID());
                itemEntity.setNoPickUpDelay();
            }
        }
    }
}

