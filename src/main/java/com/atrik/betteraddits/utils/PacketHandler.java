package com.atrik.betteraddits.utils;

import com.atrik.betteraddits.BetterAddits;
import com.atrik.betteraddits.utils.ServerMessagePackets.BuyBtnMessagePacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(BetterAddits.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    private PacketHandler() {
    }

    public static void init() {
        int index = 0;
        INSTANCE.messageBuilder(BuyBtnMessagePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(BuyBtnMessagePacket::encode).decoder(BuyBtnMessagePacket::new)
                .consumer(BuyBtnMessagePacket::handle).add();
    }
}
