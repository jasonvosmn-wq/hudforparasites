package com.jasonvosmn.parasiteshud.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item {
    public ItemBase(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.MATERIALS); // Предмет появится во вкладке "Материалы"
        setMaxStackSize(64);
    }
}