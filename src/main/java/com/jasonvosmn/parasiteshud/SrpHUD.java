package com.jasonvosmn.parasiteshud;

import com.jasonvosmn.parasiteshud.hud.ParasitesHUD;
import com.jasonvosmn.parasiteshud.proxy.CommonProxy;
import com.jasonvosmn.parasiteshud.util.ConfigLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = SrpHUD.MODID, name = "parasiteshud", version = SrpHUD.VERSION, dependencies = "required-after:forge@[14.23.5.2847,);required-after:srparasites@[1.9.21,)")

public class SrpHUD {
    public static final String MODID = "parasiteshud";
    public static final String VERSION = "0.1";



    @SidedProxy(clientSide = "com.jasonvosmn.parasiteshud.proxy.ClientProxy", serverSide = "com.jasonvosmn.parasiteshud.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        ConfigLoader.initConfig(event);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        MinecraftForge.EVENT_BUS.register(new ParasitesHUD());

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);

    }

}
