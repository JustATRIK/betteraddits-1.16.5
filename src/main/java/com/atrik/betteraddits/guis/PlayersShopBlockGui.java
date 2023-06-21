package com.atrik.betteraddits.guis;

import com.atrik.betteraddits.blocks.entities.PlayersShopBlockEntity;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.registries.ForgeRegistries;

import javax.swing.*;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PlayersShopBlockGui extends Container {
    private final IInventory inventory;
    public CompoundNBT nbtData;
    PlayersShopBlockEntity tileEntity;
    public PlayersShopBlockGui(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        this(id, playerInventory, new PlayersShopBlockEntity());
        nbtData = buffer.readNbt();
        tileEntity = (PlayersShopBlockEntity) playerInventory.player.level.getBlockEntity(new BlockPos(nbtData.getInt("x"), nbtData.getInt("y"), nbtData.getInt("z")));
    }


    public PlayersShopBlockGui(int id, PlayerInventory playerInventory, IInventory inventory) {
        super(ModMenuRegister.PLAYERS_SHOP_BLOCK_GUI.get(), id);
        this.inventory = inventory;
        this.addSlot(new Slot(this.inventory, 0, 75, 31) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
            @Override
            public boolean mayPickup(PlayerEntity playerEntity) {
                return false;
            }
            @Override
            public int getMaxStackSize() {
                return 4096;
            }
            @Override
            @OnlyIn(Dist.CLIENT)
            public boolean isActive() {
                return false;
            }
        });
        for (int y = 1; y < 4; ++y) {
            for (int x = 0; x < 9; ++x) {
                int index = x + y * 9;
                int posX = 3 + x * 18;
                int posY = 99 + (y - 1) * 18;
                this.addSlot(new Slot(playerInventory, index, posX, posY));
            }
        }
        for (int x = 0; x < 9; x++) {
            int posX = 3 + x * 18;
            int posY = 157;
            this.addSlot(new Slot(playerInventory, x, posX, posY));
        }

    }


    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.inventory.stillValid(player);
    }

    @Override
    public ItemStack clicked(int p_184996_1_, int p_184996_2_, ClickType p_184996_3_, PlayerEntity p_184996_4_) {
        if (p_184996_1_ == 0 || p_184996_3_ == ClickType.QUICK_MOVE){
            return ItemStack.EMPTY;
        }
        Method privateMethod
                = null;
        try {
            privateMethod = Container.class.getDeclaredMethod("doClick", int.class, int.class, ClickType.class, PlayerEntity.class);
            privateMethod.setAccessible(true);
            return (ItemStack) privateMethod.invoke(this, p_184996_1_, p_184996_2_, p_184996_3_, p_184996_4_);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }



    }

    public IInventory getInventory() {
        return this.inventory;
    }
    public PlayersShopBlockEntity getTileEntity(){
        return this.tileEntity;
    }
}
