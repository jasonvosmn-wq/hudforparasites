package com.jasonvosmn.parasiteshud.util;

import com.jasonvosmn.parasiteshud.items.ItemBase;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegistryHandler {

    public static final Item RUBY = new ItemBase("srpscaner");

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(RUBY);
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        // Регистрация модели (чтобы предмет отображался в руке)
        ModelLoader.setCustomModelResourceLocation(RUBY, 0, new ModelResourceLocation(RUBY.getRegistryName(), "inventory"));
    }
}
