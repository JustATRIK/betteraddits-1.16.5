package com.atrik.betteraddits.blocks.entities;

import com.atrik.betteraddits.BetterAddits;
import com.atrik.betteraddits.blocks.BlocksRegister;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.EnchantingTableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModTileEntitiesTypes {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BetterAddits.MOD_ID);
    public static final RegistryObject<TileEntityType<PlayersShopBlockEntity>> PLAYER_SHOP = TILE_ENTITIES.register(
            "player_shop", () -> TileEntityType.Builder.of(PlayersShopBlockEntity::new, BlocksRegister.PLAYERS_SHOP.get()).build(null));

    public static void registerTileEntities(IEventBus eventBus){
        TILE_ENTITIES.register(eventBus);
    }
}
