package com.atrik.betteraddits.blocks.entities;

import com.atrik.betteraddits.guis.PlayersShopBlockGui;
import com.atrik.betteraddits.guis.PlayersShopBlockScreen;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.lang.reflect.Array;

import static com.atrik.betteraddits.utils.ItemUtils.getItemByStrID;

public class PlayersShopBlockEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {
    private NonNullList<ItemStack> items;
    private final LazyOptional<? extends IItemHandler>[] handlers;
    public  CompoundNBT nbtData;
    public String sellItemID = "minecraft:stone";
    public int inStock = 0;


    public PlayersShopBlockEntity() {
        super(ModTileEntitiesTypes.PLAYER_SHOP.get());
        this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.betteraddits.shop_block.name");
    }


    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getItem(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ItemStackHelper.removeItem(items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStackHelper.takeItem(items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.set(index, stack);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.level != null
                && this.level.getBlockEntity(this.worldPosition) == this
                && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ()) <= 64;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public void tick() {
        if (!level.isClientSide){
            inStock += this.getItem(0).getCount();
            this.removeItem(0, this.getItem(0).getCount());
        }
    }
    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new PlayersShopBlockGui(id, playerInventory, this);

    }
    public void encodeExtraData(PacketBuffer buffer) {
        this.nbtData = this.serializeNBT();
        buffer.writeNbt(this.nbtData);
    }

    @Override
    public void load(BlockState state, CompoundNBT tags) {
        super.load(state, tags);
        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tags, this.items);
        this.sellItemID = tags.getString("sellItemID");
        this.inStock = tags.getInt("inStock");
        this.nbtData = tags;
    }

    @Override
    public CompoundNBT save(CompoundNBT tags) {
        super.save(tags);
        ItemStackHelper.saveAllItems(tags, this.items);
        tags.putString("sellItemID", this.sellItemID);
        tags.putInt("inStock", this.inStock);
        return tags;
    }

    @Override
    public int[] getSlotsForFace(Direction p_180463_1_) {
        return new int[1];
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_180462_1_, ItemStack p_180462_2_, @Nullable Direction p_180462_3_) {
        return p_180462_2_.getItem() == getItemByStrID(sellItemID);
    }

    @Override
    public boolean canTakeItemThroughFace(int p_180461_1_, ItemStack p_180461_2_, Direction p_180461_3_) {
        return false;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.UP) {
                return this.handlers[0].cast();
            } else if (side == Direction.DOWN) {
                return this.handlers[1].cast();
            } else {
                return this.handlers[2].cast();
            }
        } else {
            return super.getCapability(cap, side);
        }
    }
    @Override
    public void setRemoved() {
        super.setRemoved();

        for (LazyOptional<? extends IItemHandler> handler : this.handlers) {
            handler.invalidate();
        }
    }
}
