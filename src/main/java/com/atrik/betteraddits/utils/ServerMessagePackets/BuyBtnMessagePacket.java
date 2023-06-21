package com.atrik.betteraddits.utils.ServerMessagePackets;

import com.atrik.betteraddits.blocks.entities.PlayersShopBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static com.atrik.betteraddits.guis.PlayersShopBlockScreen.buyButtonPressed;
import static com.atrik.betteraddits.utils.ItemUtils.getItemByStrID;

public class BuyBtnMessagePacket {

    public final short quantity;
    public final BlockPos blockPos;


    public BuyBtnMessagePacket(short quantity, BlockPos blockPos) {
        this.quantity = quantity;
        this.blockPos = blockPos;
    }

    public BuyBtnMessagePacket(PacketBuffer byteBuf){
        this(byteBuf.readShort(), byteBuf.readBlockPos());
    }
    public void encode(PacketBuffer byteBuf){
        byteBuf.writeShort(this.quantity);
        byteBuf.writeBlockPos(this.blockPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final AtomicBoolean success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            PlayerEntity playerEntity = ctx.get().getSender();
            PlayersShopBlockEntity tileEntity = (PlayersShopBlockEntity) ctx.get().getSender().getLevel().getBlockEntity(blockPos);
            PlayerInventory playerInventory = playerEntity.inventory;
            buyButtonPressed(playerInventory, tileEntity, quantity);
            success.set(true);
        });

        ctx.get().setPacketHandled(true);
        return success.get();
    }

}
