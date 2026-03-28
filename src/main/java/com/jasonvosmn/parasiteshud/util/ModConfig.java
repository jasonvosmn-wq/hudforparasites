package com.jasonvosmn.parasiteshud.util;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = "parasiteshud", name = "parasiteshud")
@Mod.EventBusSubscriber(modid = "parasiteshud")
public class ModConfig {

    @Config.Name("HUD Scale")
    public static double hudScale = 1.0;

    @Config.Name("HUD X")
    public static int hudX = 10; // Координата по умолчанию

    @Config.Name("HUD Y")
    public static int hudY = 10; // Координата по умолчанию

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals("parasiteshud")) {
            ConfigManager.sync("parasiteshud", Config.Type.INSTANCE);
        }
    }
}