package com.atrik.betteraddits.items;

import com.atrik.betteraddits.BetterAddits;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemsRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BetterAddits.MOD_ID);

    public static final RegistryObject<Item> ADMIN_CONFIG_TOOL =ITEMS.register("admin_config_tool", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS).stacksTo(32)));


    public static void registerItems(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
