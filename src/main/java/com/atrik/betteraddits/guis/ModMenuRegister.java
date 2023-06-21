package com.atrik.betteraddits.guis;

import com.atrik.betteraddits.BetterAddits;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModMenuRegister {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, BetterAddits.MOD_ID);
    public static final RegistryObject<ContainerType<PlayersShopBlockGui>> PLAYERS_SHOP_BLOCK_GUI = register("player_shop_block_gui", PlayersShopBlockGui::new);

    private ModMenuRegister() {}

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        ScreenManager.register(PLAYERS_SHOP_BLOCK_GUI.get(), PlayersShopBlockScreen::new);
    }

    private static <T extends Container> RegistryObject<ContainerType<T>> register(String name, IContainerFactory<T> factory) {
        return CONTAINERS.register(name, () -> IForgeContainerType.create(factory));
    }
}
