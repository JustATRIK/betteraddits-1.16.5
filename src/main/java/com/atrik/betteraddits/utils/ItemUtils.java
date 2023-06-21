package com.atrik.betteraddits.utils;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemUtils {
    public static Item getItemByStrID(String id){
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id.substring(0, id.indexOf(":")), id.substring(id.indexOf(":") + 1)));
        if (item != null) return item;
        return Items.AIR;
    }
}
